package com.damonx.resilience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Resilience4jSpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(Resilience4jSpringBootDemoApplication.class, args);
    }
}
