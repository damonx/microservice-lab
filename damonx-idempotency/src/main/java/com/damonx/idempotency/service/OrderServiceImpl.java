package com.damonx.idempotency.service;

import com.damonx.idempotency.model.IdempotencyKeyEntry;
import com.damonx.idempotency.model.Order;
import com.damonx.idempotency.repository.IdempotencyKeyRepository;
import com.damonx.idempotency.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService
{
    private final OrderRepository orderRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderServiceImpl(OrderRepository orderRepository, IdempotencyKeyRepository idempotencyKeyRepository)
    {
        this.orderRepository = orderRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Override
    @Transactional
    public Order createOrUpsertOrder(String idempotencyKey, Order input) throws Exception
    {
        final Optional<IdempotencyKeyEntry> existingKey = idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey);
        if (existingKey.isPresent()) {
            Long resId = existingKey.get().getResourceId();
            return orderRepository.findById(resId).orElse(null);
        }

        // Upsert by externalId
        final Optional<Order> found = orderRepository.findByExternalId(input.getExternalId());
        final Order saved = found.map(o -> {
            // We'll update amount and keep status unless status is more 'final' already
            o.setAmount(input.getAmount());
            return orderRepository.save(o);
        }).orElseGet(() -> {
            input.setStatus("CREATED");
            return orderRepository.save(input);
        });

        final String respJson = mapper.writeValueAsString(saved);
        final IdempotencyKeyEntry entry = new IdempotencyKeyEntry(idempotencyKey, "CREATE_ORDER", saved.getId(), respJson);
        idempotencyKeyRepository.save(entry);
        return saved;
    }

    @Override
    public Optional<Order> getOrderById(final Long orderId)
    {
        return orderRepository.findById(orderId);
    }
}
