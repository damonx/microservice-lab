package com.example.demo.controller;

import com.example.demo.service.BusinessService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final BusinessService businessService;
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    public HelloController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/hello")
    @Timed(value = "demo.hello.latency", description = "Latency of hello endpoint")
    public ResponseEntity<String> hello(@RequestParam(name = "name", required = false, defaultValue = "world") String name) {
        try {
            String result = businessService.performWork();
            return ResponseEntity.ok("Hello, " + name + "! status=" + result);
        } catch (Exception ex) {
            logger.error("Error in hello", ex);
            return ResponseEntity.internalServerError().body("Error: " + ex.getMessage());
        }
    }
}
