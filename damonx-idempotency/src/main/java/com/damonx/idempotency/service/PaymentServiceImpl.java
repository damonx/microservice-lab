package com.damonx.idempotency.service;

import com.damonx.idempotency.model.IdempotencyKeyEntry;
import com.damonx.idempotency.model.Payment;
import com.damonx.idempotency.repository.IdempotencyKeyRepository;
import com.damonx.idempotency.repository.OrderRepository;
import com.damonx.idempotency.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService
{
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              IdempotencyKeyRepository idempotencyKeyRepository)
    {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(final String idempotencyKey, final Payment input) throws Exception
    {
        final Optional<IdempotencyKeyEntry> existingKey = idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey);
        if (existingKey.isPresent()) {
            Long resId = existingKey.get().getResourceId();
            return paymentRepository.findById(resId).orElse(null);
        }

        // Conditional operations & upsert semantics:
        // If a payment already exists for this externalId, return it (idempotent).
        final Optional<Payment> existingPayment = paymentRepository.findByExternalId(input.getExternalId());
        if (existingPayment.isPresent()) {
            final Payment p = existingPayment.get();
            // If already COMPLETED, skip (conditional)
            if ("COMPLETED".equalsIgnoreCase(p.getStatus())) {
                // store idempotency mapping for this key (so future requests with same key will be fast)
                IdempotencyKeyEntry entry = new IdempotencyKeyEntry(idempotencyKey, "CREATE_PAYMENT", p.getId(), mapper.writeValueAsString(p));
                idempotencyKeyRepository.save(entry);
                return p;
            }
            // Otherwise, update amount/status etc.
            p.setAmount(input.getAmount());
            p.setStatus("COMPLETED");
            final Payment saved = paymentRepository.save(p);
            IdempotencyKeyEntry entry = new IdempotencyKeyEntry(idempotencyKey, "CREATE_PAYMENT", saved.getId(), mapper.writeValueAsString(saved));
            idempotencyKeyRepository.save(entry);
            // optionally update corresponding order status
            if (input.getExternalId() != null) {
                orderRepository.findByExternalId(input.getExternalId()).ifPresent(order -> {
                    order.setStatus("PAID");
                    orderRepository.save(order);
                });
            }
            return saved;
        }

        // No existing payment found -> create
        input.setStatus("COMPLETED");
        final Payment saved = paymentRepository.save(input);

        // For conditional: update order status to PAID if exists
        if (input.getExternalId() != null) {
            orderRepository.findByExternalId(input.getExternalId()).ifPresent(order -> {
                // only update if not already PAID
                if (!"PAID".equalsIgnoreCase(order.getStatus())) {
                    order.setStatus("PAID");
                    orderRepository.save(order);
                }
            });
        }

        final IdempotencyKeyEntry entry = new IdempotencyKeyEntry(idempotencyKey, "CREATE_PAYMENT", saved.getId(), mapper.writeValueAsString(saved));
        idempotencyKeyRepository.save(entry);
        return saved;
    }

    /**
     * Retrieves a payment by its internal ID.
     *
     * @param paymentId the internal database ID of the payment
     * @return an Optional containing the payment if found
     */
    @Override
    public Optional<Payment> getPaymentById(final Long paymentId) {
        return paymentRepository.findById(paymentId);
    }
}
