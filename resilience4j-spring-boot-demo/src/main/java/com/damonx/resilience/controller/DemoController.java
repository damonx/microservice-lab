package com.damonx.resilience.controller;

import com.damonx.resilience.model.Payment;
import com.damonx.resilience.service.ResilientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final ResilientService svc;
    public DemoController(ResilientService svc) { this.svc = svc; }

    @PostMapping("/payments/{id}")
    public ResponseEntity<Payment> createPayment(@PathVariable String id) {
        Payment p = svc.createPayment(id);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(svc.getPaymentFromCache(id));
    }

    @GetMapping("/bulk")
    public ResponseEntity<String> bulk() {
        return ResponseEntity.ok(svc.bulkheadProtectedOperation());
    }

    @GetMapping("/timed/{id}")
    public CompletionStage<ResponseEntity<Payment>> timed(@PathVariable String id) {
        return svc.timeLimitedOperation(id)
            .toCompletableFuture()
            .thenApply(ResponseEntity::ok)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(ex -> ResponseEntity.status(504).build());
    }
}
