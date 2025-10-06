package com.portfolio.campaignmanager.repository;

import com.portfolio.campaignmanager.model.entity.CampaignMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for CampaignMetric entity operations.
 * Provides data access methods for campaign metrics and analytics.
 */
@Repository
public interface CampaignMetricRepository extends JpaRepository<CampaignMetric, UUID> {
    
    /**
     * Finds all metrics for a specific campaign.
     *
     * @param campaignId The ID of the campaign
     * @return List of metrics for the campaign
     */
    List<CampaignMetric> findByCampaignId(UUID campaignId);
    
    /**
     * Finds metrics for a campaign within a specific date range.
     *
     * @param campaignId The ID of the campaign
     * @param start The start date (inclusive)
     * @param end The end date (inclusive)
     * @return List of metrics within the date range
     */
    List<CampaignMetric> findByCampaignIdAndDateBetween(UUID campaignId, LocalDate start, LocalDate end);
}