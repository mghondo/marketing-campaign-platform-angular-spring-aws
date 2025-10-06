package com.portfolio.campaignmanager.service;

import com.portfolio.campaignmanager.exception.ResourceNotFoundException;
import com.portfolio.campaignmanager.model.dto.AuthRequest;
import com.portfolio.campaignmanager.model.dto.AuthResponse;
import com.portfolio.campaignmanager.model.dto.RegisterRequest;
import com.portfolio.campaignmanager.model.entity.User;
import com.portfolio.campaignmanager.model.enums.UserRole;
import com.portfolio.campaignmanager.repository.UserRepository;
import com.portfolio.campaignmanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling user authentication and registration.
 * Manages user login, registration, and JWT token generation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user account.
     *
     * @param request Registration request containing user details
     * @return AuthResponse with JWT token and user information
     * @throws DataIntegrityViolationException if email already exists
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new DataIntegrityViolationException("Email already exists: " + request.getEmail());
        }
        
        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(UserRole.USER);
        
        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser);
        
        return AuthResponse.of(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole().name()
        );
    }

    /**
     * Authenticate user and generate JWT token.
     *
     * @param request Authentication request containing email and password
     * @return AuthResponse with JWT token and user information
     * @throws BadCredentialsException if credentials are invalid
     */
    public AuthResponse login(AuthRequest request) {
        log.info("Attempting to authenticate user with email: {}", request.getEmail());
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            // Load user from database
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
            
            log.info("User authenticated successfully: {}", user.getEmail());
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user);
            
            return AuthResponse.of(
                    token,
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole().name()
            );
            
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}