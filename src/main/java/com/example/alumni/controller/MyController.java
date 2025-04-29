package com.example.alumni.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from React frontend
public class MyController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Spring Boot!";
    }
}
