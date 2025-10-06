package com.portfolio.campaignmanager.model.dto;

import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for campaign performance summary.
 * Aggregates metrics across all dates for a specific campaign.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignPerformanceResponse {

    /**
     * Campaign unique identifier.
     */
    private UUID campaignId;

    /**
     * Campaign name.
     */
    private String campaignName;

    /**
     * Current campaign status.
     */
    private CampaignStatus status;

    /**
     * Total impressions across all metrics for this campaign.
     */
    private long totalImpressions;

    /**
     * Total clicks across all metrics for this campaign.
     */
    private long totalClicks;

    /**
     * Total conversions across all metrics for this campaign.
     */
    private long totalConversions;

    /**
     * Overall click-through rate as percentage (total clicks/total impressions * 100).
     * Returns 0.0 if no impressions exist.
     */
    private double clickThroughRate;

    /**
     * Overall conversion rate as percentage (total conversions/total clicks * 100).
     * Returns 0.0 if no clicks exist.
     */
    private double conversionRate;

    /**
     * Static factory method to create CampaignPerformanceResponse with calculated rates.
     */
    public static CampaignPerformanceResponse create(UUID campaignId, String campaignName, 
                                                   CampaignStatus status, long totalImpressions, 
                                                   long totalClicks, long totalConversions) {
        double ctr = totalImpressions > 0 ? 
                (double) totalClicks / totalImpressions * 100 : 0.0;
        
        double conversionRate = totalClicks > 0 ? 
                (double) totalConversions / totalClicks * 100 : 0.0;

        return CampaignPerformanceResponse.builder()
                .campaignId(campaignId)
                .campaignName(campaignName)
                .status(status)
                .totalImpressions(totalImpressions)
                .totalClicks(totalClicks)
                .totalConversions(totalConversions)
                .clickThroughRate(Math.round(ctr * 100.0) / 100.0) // Round to 2 decimal places
                .conversionRate(Math.round(conversionRate * 100.0) / 100.0) // Round to 2 decimal places
                .build();
    }
}