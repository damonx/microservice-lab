package com.example.hellodamonx;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello DamonX - Today is " + LocalDate.now();
    }
}