package com.example.JwtSecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/secure")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello from secured URL");
    }

    @GetMapping("/secure/admin")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Admin Only");
    }
}
