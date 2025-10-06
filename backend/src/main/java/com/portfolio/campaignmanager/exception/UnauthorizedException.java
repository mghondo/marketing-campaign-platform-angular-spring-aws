package com.portfolio.campaignmanager.exception;

/**
 * Exception thrown when a user attempts to access a resource they don't own
 * or don't have permission to access.
 * This typically results in a 403 FORBIDDEN HTTP response.
 */
public class UnauthorizedException extends RuntimeException {
    
    /**
     * Creates a new UnauthorizedException with the specified message.
     *
     * @param message The detail message explaining the authorization failure
     */
    public UnauthorizedException(String message) {
        super(message);
    }
    
    /**
     * Creates a new UnauthorizedException with a message and cause.
     *
     * @param message The detail message explaining the authorization failure
     * @param cause The root cause of the exception
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}