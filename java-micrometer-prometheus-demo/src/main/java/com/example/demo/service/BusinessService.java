package com.example.demo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class BusinessService {

    private final Counter requestCounter;

    public BusinessService(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("demo.requests.total")
                .description("Total number of requests")
                .register(meterRegistry);
    }

    public String performWork() {
        requestCounter.increment();
        int n = ThreadLocalRandom.current().nextInt(100);
        if (n < 85) {
            return "ok";
        } else {
            throw new RuntimeException("simulated error");
        }
    }
}
