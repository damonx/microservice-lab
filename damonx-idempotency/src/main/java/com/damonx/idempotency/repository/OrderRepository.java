package com.damonx.idempotency.repository;

import com.damonx.idempotency.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByExternalId(String externalId);
}
