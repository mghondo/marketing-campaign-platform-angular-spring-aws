package com.portfolio.campaignmanager.controller;

import com.portfolio.campaignmanager.model.dto.AuthRequest;
import com.portfolio.campaignmanager.model.dto.AuthResponse;
import com.portfolio.campaignmanager.model.dto.RegisterRequest;
import com.portfolio.campaignmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints.
 * Handles user registration and login requests.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user account.
     *
     * @param request Registration request containing user details
     * @return AuthResponse with JWT token and user information
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request received for email: {}", request.getEmail());
        
        AuthResponse response = authService.register(request);
        
        log.info("User registered successfully: {}", response.getEmail());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Authenticate user and generate JWT token.
     *
     * @param request Authentication request containing email and password
     * @return AuthResponse with JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login request received for email: {}", request.getEmail());
        
        AuthResponse response = authService.login(request);
        
        log.info("User logged in successfully: {}", response.getEmail());
        return ResponseEntity.ok(response);
    }
}