package com.damonx.idempotency.repository;

import com.damonx.idempotency.model.IdempotencyKeyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntry, Long> {
    Optional<IdempotencyKeyEntry> findByIdempotencyKey(String key);
}
