package com.damonx.resilience.service;

import com.damonx.resilience.model.Payment;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ResilientService {

    // CircuitBreaker + Retry + RateLimiter example
    @CircuitBreaker(name = "demoServiceCB", fallbackMethod = "fallbackCreatePayment")
    @Retry(name = "demoServiceRetry")
    @RateLimiter(name = "demoServiceRL")
    public Payment createPayment(String id) {
        // simulate transient failure
        maybeFail();
        // simulate business logic
        return new Payment(id, ThreadLocalRandom.current().nextDouble(10, 100));
    }

    // Bulkhead example (separate endpoint)
    @Bulkhead(name = "demoServiceBH", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "fallbackBulkhead")
    public String bulkheadProtectedOperation() {
        // simulate work
        sleep(500);
        return "ok";
    }

    // TimeLimiter example - must return CompletionStage/Future for annotation proxying
    @TimeLimiter(name = "demoServiceTL")
    public CompletionStage<Payment> timeLimitedOperation(String id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(3000); // will exceed the 2s time limiter from config, causing timeout
            return new Payment(id, 42.0);
        });
    }

    // Cache example
    @Cacheable(value = "payments", key = "#id")
    public Payment getPaymentFromCache(String id) {
        // expensive operation simulated
        sleep(300);
        return new Payment(id, ThreadLocalRandom.current().nextDouble(10, 100));
    }

    // Fallbacks
    public Payment fallbackCreatePayment(String id, Throwable t) {
        return new Payment("fallback-" + id, 0.0);
    }

    public String fallbackBulkhead(Throwable t) {
        return "bulkhead-fallback";
    }

    // helpers
    private void maybeFail() {
        if (ThreadLocalRandom.current().nextInt(10) < 3) { // 30% fail
            throw new RuntimeException("simulated transient error");
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(Duration.ofMillis(ms)); } catch (InterruptedException ignored) {}
    }
}
