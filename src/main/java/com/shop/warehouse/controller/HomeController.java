package com.shop.warehouse.controller;

import org.springframework.web.bind.annotation.GetMapping; import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController public class HomeController {


@GetMapping("/")
public Map<String, String> home() {
    return Map.of(
        "application", "Warehouse Management API",
        "status", "running",
        "message", "Welcome to Warehouse Management API"
    );
}

@GetMapping("/health")
public Map<String, String> health() {
    return Map.of(
        "status", "UP"
    );
}
}