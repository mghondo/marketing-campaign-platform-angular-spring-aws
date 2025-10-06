package com.portfolio.campaignmanager.repository;

import com.portfolio.campaignmanager.model.entity.Campaign;
import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Campaign entity operations.
 * Provides data access methods for campaign management.
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    
    /**
     * Finds all campaigns owned by a specific user.
     *
     * @param userId The ID of the user
     * @return List of campaigns owned by the user
     */
    List<Campaign> findByUserId(UUID userId);
    
    /**
     * Finds campaigns owned by a specific user with a specific status.
     *
     * @param userId The ID of the user
     * @param status The campaign status to filter by
     * @return List of campaigns matching the criteria
     */
    List<Campaign> findByUserIdAndStatus(UUID userId, CampaignStatus status);
    
    /**
     * Counts the total number of campaigns owned by a user.
     *
     * @param userId The ID of the user
     * @return Total count of campaigns for the user
     */
    long countByUserId(UUID userId);
    
    /**
     * Counts campaigns owned by a user with a specific status.
     *
     * @param userId The ID of the user
     * @param status The campaign status to filter by
     * @return Count of campaigns matching the criteria
     */
    long countByUserIdAndStatus(UUID userId, CampaignStatus status);
}