package com.damonx.idempotency.controller;

import com.damonx.idempotency.model.Payment;
import com.damonx.idempotency.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestHeader("Idempotency-Key") String idKey, @RequestBody Payment payment) throws Exception
    {
        Payment saved = paymentService.createPayment(idKey, payment);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        return paymentService.getPaymentById(paymentId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
