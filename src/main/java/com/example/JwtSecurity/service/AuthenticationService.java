package com.example.JwtSecurity.service;

import com.example.JwtSecurity.model.AuthenticationResponse;
import com.example.JwtSecurity.model.Customer;
import com.example.JwtSecurity.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(CustomerRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(Customer request) {
        Customer customer = new Customer();

        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(request.getRole());

        customer = repository.save(customer);

        String token = jwtService.generateToken(customer);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(Customer request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Customer customer = repository.findCustomerByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(customer);

        return new AuthenticationResponse(token);
    }
}
