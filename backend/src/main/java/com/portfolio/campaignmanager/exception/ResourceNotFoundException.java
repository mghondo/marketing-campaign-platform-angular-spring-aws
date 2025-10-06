package com.portfolio.campaignmanager.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * This typically results in a 404 NOT FOUND HTTP response.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Creates a new ResourceNotFoundException with the specified message.
     *
     * @param message The detail message explaining what resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Creates a new ResourceNotFoundException with a message and cause.
     *
     * @param message The detail message explaining what resource was not found
     * @param cause The root cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}