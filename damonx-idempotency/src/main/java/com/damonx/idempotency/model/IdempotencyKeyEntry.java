package com.damonx.idempotency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "idempotency_keys", indexes = {
    @Index(name = "idx_key_operation", columnList = "idempotencyKey, operation")
})
public class IdempotencyKeyEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 128)
    private String idempotencyKey;

    @Column(nullable = false)
    private String operation; // e.g. "CREATE_PAYMENT", "CREATE_ORDER", "UPSERT_USER"

    @Column
    private Long resourceId; // persisted resource id

    @Lob
    @Column(columnDefinition = "text")
    private String responseJson;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // Constructors, getters, setters
    public IdempotencyKeyEntry() {}

    public IdempotencyKeyEntry(String idempotencyKey, String operation, Long resourceId, String responseJson) {
        this.idempotencyKey = idempotencyKey;
        this.operation = operation;
        this.resourceId = resourceId;
        this.responseJson = responseJson;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    public String getResponseJson() { return responseJson; }
    public void setResponseJson(String responseJson) { this.responseJson = responseJson; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
