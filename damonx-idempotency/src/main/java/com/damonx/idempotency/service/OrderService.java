package com.damonx.idempotency.service;

import com.damonx.idempotency.model.Order;

import java.util.Optional;

/**
 * Service responsible for creating or updating orders in an idempotent way.
 *
 * <p>Implementations must guarantee that:</p>
 * <ul>
 *     <li>If the same {@code idempotencyKey} was processed before, the previously
 *         stored result is returned immediately.</li>
 *     <li>If an order with the same {@code externalId} exists, it is updated (upsert behavior).</li>
 *     <li>If no existing order is found, a new one is created with status {@code CREATED}.</li>
 * </ul>
 */
public interface OrderService {

    /**
     * Creates or upserts an order based on an idempotency key and external identifier.
     *
     * @param idempotencyKey unique identifier used to ensure idempotency across requests
     * @param input          order request payload
     * @return the created or updated {@link Order}
     * @throws Exception if serialization or persistence fails
     */
    Order createOrUpsertOrder(String idempotencyKey, Order input) throws Exception;

    /**
     * Retrieves an order by its internal ID.
     *
     * @param orderId the internal database ID
     * @return an {@link Optional} containing the order if found
     */
    Optional<Order> getOrderById(Long orderId);
}