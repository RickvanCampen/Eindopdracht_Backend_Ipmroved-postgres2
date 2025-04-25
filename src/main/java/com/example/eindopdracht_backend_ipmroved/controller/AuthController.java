package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.dto.requests.AuthRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateUserRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AuthResponse;
import com.example.eindopdracht_backend_ipmroved.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(201).body(service.registerUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
