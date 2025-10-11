package com.damonx.idempotency.service;

import com.damonx.idempotency.model.Payment;

import java.util.Optional;

/**
 * Service responsible for safely creating payments in an idempotent manner.
 *
 * <p>
 * Implementations must enforce the following rules:
 * <ul>
 *     <li>If a request with the same {@code idempotencyKey} was processed before,
 *         return the previously stored result.</li>
 *     <li>If a payment with the same {@code externalId} already exists:
 *         <ul>
 *             <li>If its status is {@code COMPLETED}, return it without modification.</li>
 *             <li>Otherwise, update and persist it (upsert semantics).</li>
 *         </ul>
 *     </li>
 *     <li>If no existing payment is found, create a new one and store an idempotency record.</li>
 * </ul>
 * </p>
 */
public interface PaymentService {

    /**
     * Creates or retrieves a payment based on idempotency rules.
     *
     * @param idempotencyKey unique identifier to ensure idempotency across repeated requests
     * @param input          the payment request payload
     * @return a persisted {@link Payment} instance
     * @throws Exception if serialization or persistence fails
     */
    Payment createPayment(String idempotencyKey, Payment input) throws Exception;

    /**
     * Retrieves a payment by its internal ID.
     *
     * @param paymentId the internal database ID of the payment
     * @return an {@link Optional} containing the payment if found
     */
    Optional<Payment> getPaymentById(Long paymentId);
}
