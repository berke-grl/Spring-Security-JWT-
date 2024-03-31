package com.example.JwtSecurity.controller;

import com.example.JwtSecurity.model.AuthenticationResponse;
import com.example.JwtSecurity.model.Customer;
import com.example.JwtSecurity.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Customer customer) {
        return ResponseEntity.ok(authenticationService.register(customer));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody Customer customer) {
        return ResponseEntity.ok(authenticationService.authenticate(customer));
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
