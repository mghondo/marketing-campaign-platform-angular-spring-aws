package com.portfolio.campaignmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for authentication responses.
 * Contains JWT token and user information after successful login/registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private UUID userId;
    private String email;
    private String name;
    private String role;
    
    /**
     * Constructor for creating response with all user details
     */
    public static AuthResponse of(String token, UUID userId, String email, String name, String role) {
        return new AuthResponse(token, userId, email, name, role);
    }
}