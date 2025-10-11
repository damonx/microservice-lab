package com.damonx.idempotency.controller;

import com.damonx.idempotency.model.User;
import com.damonx.idempotency.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping
    public ResponseEntity<User> upsertUser(@RequestHeader("Idempotency-Key") String idKey, @RequestBody User user) throws JsonProcessingException {
        final User saved = userService.upsertUser(idKey, user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userEmailId}")
    public ResponseEntity<User> getUserByEmailId(@PathVariable String userEmailId) {
        return userService.getUserByEmail(userEmailId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

    }
}