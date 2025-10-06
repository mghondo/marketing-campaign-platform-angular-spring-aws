package com.portfolio.campaignmanager.repository;

import com.portfolio.campaignmanager.model.entity.CampaignAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for CampaignAsset entity operations.
 * Provides data access methods for campaign asset management.
 */
@Repository
public interface CampaignAssetRepository extends JpaRepository<CampaignAsset, UUID> {
    
    /**
     * Finds all assets associated with a specific campaign.
     *
     * @param campaignId The ID of the campaign
     * @return List of assets for the campaign
     */
    List<CampaignAsset> findByCampaignId(UUID campaignId);
    
    /**
     * Finds an asset by its S3 key.
     *
     * @param s3Key The unique S3 key of the asset
     * @return Optional containing the asset if found, empty otherwise
     */
    Optional<CampaignAsset> findByS3Key(String s3Key);
}