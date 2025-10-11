package com.damonx.idempotency.controller;

import com.damonx.idempotency.model.Order;
import com.damonx.idempotency.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public ResponseEntity<Order> createOrUpsertOrder(@RequestHeader("Idempotency-Key") String idKey, @RequestBody Order order) throws Exception
    {
        Order saved = orderService.createOrUpsertOrder(idKey, order);
        return ResponseEntity.ok(saved);
    }

    /**
     * Get Order by ID.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
