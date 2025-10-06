package com.portfolio.campaignmanager.repository;

import com.portfolio.campaignmanager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations.
 * Provides data access methods for user management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Checks if a user exists with the given email address.
     *
     * @param email The email address to check
     * @return true if a user exists with this email, false otherwise
     */
    boolean existsByEmail(String email);
}