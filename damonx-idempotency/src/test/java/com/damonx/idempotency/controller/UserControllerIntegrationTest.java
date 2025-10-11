package com.damonx.idempotency.controller;

import com.damonx.idempotency.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Integration tests for user controller.")
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestEntityManager
public class UserControllerIntegrationTest extends BaseIntegrationTest
{
//    User newUser = new User(null, "Charlie", "charlie@example.com", Instant.now());
//
//    // Make first request
//    webClient.post()
//        .uri("/users")
//        .header("Idempotency-Key", "upsert-user-001")
//        .bodyValue(newUser)
//        .exchange()
//        .expectStatus().isEqualTo(HttpStatus.OK);
//
//    // Make second request with same Idempotency-Key
//    webClient.post()
//        .uri("/users")
//        .header("Idempotency-Key", "upsert-user-001")
//        .bodyValue(newUser)
//        .exchange()
//        .expectStatus().isEqualTo(HttpStatus.OK);
//
//    // ✅ Now assert DB has only ONE record with that email
//    List<User> found = userRepository.findByEmail("charlie@example.com");
//    assertThat(found).hasSize(1);
}
