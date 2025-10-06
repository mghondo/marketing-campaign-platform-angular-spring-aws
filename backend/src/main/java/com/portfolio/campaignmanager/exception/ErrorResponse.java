package com.portfolio.campaignmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standardized error response structure for API error handling.
 * Used to provide consistent error information to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code.
     */
    private int status;
    
    /**
     * HTTP status reason phrase.
     */
    private String error;
    
    /**
     * Detailed error message.
     */
    private String message;
    
    /**
     * The request path where the error occurred.
     */
    private String path;
    
    /**
     * Convenience constructor for creating an error response.
     *
     * @param status HTTP status code
     * @param error HTTP status reason phrase
     * @param message Detailed error message
     * @param path Request path
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}