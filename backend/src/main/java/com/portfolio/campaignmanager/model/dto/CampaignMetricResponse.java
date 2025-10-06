package com.portfolio.campaignmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for individual campaign metric data points.
 * Represents daily performance metrics for a specific campaign.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignMetricResponse {

    /**
     * Date of the metric data.
     */
    private LocalDate date;

    /**
     * Number of impressions for this date.
     */
    private int impressions;

    /**
     * Number of clicks for this date.
     */
    private int clicks;

    /**
     * Number of conversions for this date.
     */
    private int conversions;

    /**
     * Click-through rate as percentage (clicks/impressions * 100).
     * Returns 0.0 if no impressions exist.
     */
    private double clickThroughRate;

    /**
     * Conversion rate as percentage (conversions/clicks * 100).
     * Returns 0.0 if no clicks exist.
     */
    private double conversionRate;

    /**
     * Static factory method to create CampaignMetricResponse from entity with calculated rates.
     */
    public static CampaignMetricResponse fromEntity(com.portfolio.campaignmanager.model.entity.CampaignMetric metric) {
        double ctr = metric.getImpressions() > 0 ? 
                (double) metric.getClicks() / metric.getImpressions() * 100 : 0.0;
        
        double conversionRate = metric.getClicks() > 0 ? 
                (double) metric.getConversions() / metric.getClicks() * 100 : 0.0;

        return CampaignMetricResponse.builder()
                .date(metric.getDate())
                .impressions(metric.getImpressions())
                .clicks(metric.getClicks())
                .conversions(metric.getConversions())
                .clickThroughRate(Math.round(ctr * 100.0) / 100.0) // Round to 2 decimal places
                .conversionRate(Math.round(conversionRate * 100.0) / 100.0) // Round to 2 decimal places
                .build();
    }
}