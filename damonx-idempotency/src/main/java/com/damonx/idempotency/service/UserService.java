package com.damonx.idempotency.service;

import com.damonx.idempotency.model.Payment;
import com.damonx.idempotency.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

/**
 * Service for managing user profiles with idempotent operations.
 *
 * <p>Upsert logic is recommended when creating or updating users to avoid
 * duplicates from repeated requests.</p>
 */
public interface UserService {
    /**
     * Updates or inserts a user based on idempotencyKey.
     * @param idempotencyKey the unique idempotency key.
     * @param user the user to updated or inserted.
     * @return
     * @throws JsonProcessingException
     */
    User upsertUser(String idempotencyKey, User user) throws JsonProcessingException;

    /**
     * Finds user by given email id.
     * @param email the email id in the request.
     * @return the founded user Optional instance.
     */
    Optional<User> getUserByEmail(String email);
}
