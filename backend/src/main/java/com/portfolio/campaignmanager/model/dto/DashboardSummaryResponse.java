package com.portfolio.campaignmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for dashboard summary statistics.
 * Provides aggregated metrics across all user campaigns.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    /**
     * Total number of campaigns for the user.
     */
    private int totalCampaigns;

    /**
     * Number of campaigns with ACTIVE status.
     */
    private int activeCampaigns;

    /**
     * Sum of all campaign budgets (null-safe).
     */
    private BigDecimal totalBudget;

    /**
     * Total impressions across all campaign metrics.
     */
    private long totalImpressions;

    /**
     * Total clicks across all campaign metrics.
     */
    private long totalClicks;

    /**
     * Total conversions across all campaign metrics.
     */
    private long totalConversions;

    /**
     * Average click-through rate as percentage (clicks/impressions * 100).
     * Returns 0.0 if no impressions exist.
     */
    private double averageClickThroughRate;

    /**
     * Average conversion rate as percentage (conversions/clicks * 100).
     * Returns 0.0 if no clicks exist.
     */
    private double averageConversionRate;
}