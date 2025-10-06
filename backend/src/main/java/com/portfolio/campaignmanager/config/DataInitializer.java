package com.portfolio.campaignmanager.config;

import com.portfolio.campaignmanager.model.entity.User;
import com.portfolio.campaignmanager.model.enums.UserRole;
import com.portfolio.campaignmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Data initializer to create a test user for development purposes.
 * This component runs after the application starts and creates the hardcoded user
 * if it doesn't already exist.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    
    // The hardcoded user ID used in the CampaignController
    private static final UUID HARDCODED_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    
    @Override
    public void run(String... args) throws Exception {
        initializeTestUser();
    }
    
    /**
     * Creates a test user if it doesn't already exist.
     * This user is used for testing the API endpoints with the hardcoded user ID.
     */
    private void initializeTestUser() {
        // Check by email first to avoid unique constraint violations
        if (!userRepository.existsByEmail("test@example.com")) {
            User testUser = new User();
            testUser.setId(HARDCODED_USER_ID);
            testUser.setEmail("test@example.com");
            testUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMye1nFhEt8vMr1DPCJ7BNDZl5EzUqgZ6Ni"); // "password"
            testUser.setName("Test User");
            testUser.setRole(UserRole.USER);
            
            userRepository.save(testUser);
            log.info("Created test user with ID: {} and email: {}", HARDCODED_USER_ID, testUser.getEmail());
        } else {
            log.info("Test user already exists with email: test@example.com");
        }
    }
}