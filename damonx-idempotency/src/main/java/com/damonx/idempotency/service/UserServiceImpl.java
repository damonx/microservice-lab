package com.damonx.idempotency.service;

import com.damonx.idempotency.model.IdempotencyKeyEntry;
import com.damonx.idempotency.model.User;
import com.damonx.idempotency.repository.IdempotencyKeyRepository;
import com.damonx.idempotency.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserServiceImpl(final UserRepository userRepository, final IdempotencyKeyRepository idempotencyKeyRepository)
    {
        this.userRepository = userRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Override
    @Transactional
    public User upsertUser(final String idempotencyKey, final User user) throws JsonProcessingException
    {
        // Idempotency Key handling
        final Optional<IdempotencyKeyEntry> existingKey = idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey);
        if (existingKey.isPresent()) {
            final Long resId = existingKey.get().getResourceId();
            return userRepository.findById(resId).orElse(null);
        }

        // Upsert by email
        final Optional<User> found = userRepository.findByEmail(user.getEmail());
        final User updatedUser = found.map(u -> {
            u.setName(user.getName());
            return userRepository.save(u);
        }).orElseGet(() -> userRepository.save(new User(user.getName(), user.getEmail())));

        // Persist idempotency entry with a simple JSON response
        final String respJson = mapper.writeValueAsString(updatedUser);
        final IdempotencyKeyEntry idempotencyKeyEntry = new IdempotencyKeyEntry(idempotencyKey, "UPSERT_USER", updatedUser.getId(), respJson);
        idempotencyKeyRepository.save(idempotencyKeyEntry);
        return updatedUser;
    }

    @Override
    public Optional<User> getUserByEmail(final String email)
    {
        return userRepository.findByEmail(email);
    }
}
