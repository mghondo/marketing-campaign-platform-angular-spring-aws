package com.portfolio.campaignmanager.service;

import com.portfolio.campaignmanager.exception.ResourceNotFoundException;
import com.portfolio.campaignmanager.exception.UnauthorizedException;
import com.portfolio.campaignmanager.model.dto.CampaignMetricResponse;
import com.portfolio.campaignmanager.model.dto.CampaignPerformanceResponse;
import com.portfolio.campaignmanager.model.dto.DashboardSummaryResponse;
import com.portfolio.campaignmanager.model.entity.Campaign;
import com.portfolio.campaignmanager.model.entity.CampaignMetric;
import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import com.portfolio.campaignmanager.repository.CampaignMetricRepository;
import com.portfolio.campaignmanager.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for dashboard analytics and campaign performance metrics.
 * Provides aggregated data and calculations for dashboard visualization.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final CampaignRepository campaignRepository;
    private final CampaignMetricRepository campaignMetricRepository;

    /**
     * Gets dashboard summary statistics for a user.
     * Aggregates data across all user campaigns and their metrics.
     *
     * @param userId The user ID to get summary for
     * @return Dashboard summary with aggregated metrics
     */
    public DashboardSummaryResponse getDashboardSummary(UUID userId) {
        log.debug("Calculating dashboard summary for user: {}", userId);

        List<Campaign> userCampaigns = campaignRepository.findByUserId(userId);
        
        if (userCampaigns.isEmpty()) {
            log.debug("No campaigns found for user: {}", userId);
            return createEmptyDashboardSummary();
        }

        // Get campaign IDs for metric queries
        List<UUID> campaignIds = userCampaigns.stream()
                .map(Campaign::getId)
                .collect(Collectors.toList());

        // Get all metrics for user's campaigns
        List<CampaignMetric> allMetrics = new ArrayList<>();
        for (UUID campaignId : campaignIds) {
            allMetrics.addAll(campaignMetricRepository.findByCampaignId(campaignId));
        }

        // Calculate aggregated metrics
        int totalCampaigns = userCampaigns.size();
        int activeCampaigns = (int) userCampaigns.stream()
                .filter(campaign -> campaign.getStatus() == CampaignStatus.ACTIVE)
                .count();

        BigDecimal totalBudget = userCampaigns.stream()
                .map(Campaign::getBudget)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalImpressions = allMetrics.stream()
                .mapToLong(CampaignMetric::getImpressions)
                .sum();

        long totalClicks = allMetrics.stream()
                .mapToLong(CampaignMetric::getClicks)
                .sum();

        long totalConversions = allMetrics.stream()
                .mapToLong(CampaignMetric::getConversions)
                .sum();

        // Calculate average rates
        double averageClickThroughRate = totalImpressions > 0 ? 
                (double) totalClicks / totalImpressions * 100 : 0.0;
        
        double averageConversionRate = totalClicks > 0 ? 
                (double) totalConversions / totalClicks * 100 : 0.0;

        DashboardSummaryResponse summary = DashboardSummaryResponse.builder()
                .totalCampaigns(totalCampaigns)
                .activeCampaigns(activeCampaigns)
                .totalBudget(totalBudget)
                .totalImpressions(totalImpressions)
                .totalClicks(totalClicks)
                .totalConversions(totalConversions)
                .averageClickThroughRate(Math.round(averageClickThroughRate * 100.0) / 100.0)
                .averageConversionRate(Math.round(averageConversionRate * 100.0) / 100.0)
                .build();

        log.debug("Dashboard summary calculated: {} campaigns, {} total impressions, {} total clicks", 
                totalCampaigns, totalImpressions, totalClicks);

        return summary;
    }

    /**
     * Gets campaign metrics for a specific campaign and date range.
     * Verifies user ownership before returning data.
     *
     * @param campaignId The campaign ID
     * @param userId The user ID making the request
     * @param startDate Start date for metrics (inclusive)
     * @param endDate End date for metrics (inclusive)
     * @return List of daily metrics for the date range
     */
    public List<CampaignMetricResponse> getCampaignMetrics(UUID campaignId, UUID userId, 
                                                         LocalDate startDate, LocalDate endDate) {
        log.debug("Getting campaign metrics for campaign: {}, user: {}, date range: {} to {}", 
                campaignId, userId, startDate, endDate);

        // Verify campaign exists and user owns it
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found: " + campaignId));

        if (!campaign.getUser().getId().equals(userId)) {
            log.warn("User {} attempted to access metrics for campaign {} owned by user {}", 
                    userId, campaignId, campaign.getUser().getId());
            throw new UnauthorizedException("You don't have permission to view this campaign's metrics");
        }

        // Set default date range if not provided (last 30 days)
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // Validate date range
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Get metrics for date range
        List<CampaignMetric> metrics = campaignMetricRepository
                .findByCampaignIdAndDateBetween(campaignId, startDate, endDate);

        List<CampaignMetricResponse> response = metrics.stream()
                .map(CampaignMetricResponse::fromEntity)
                .sorted(Comparator.comparing(CampaignMetricResponse::getDate))
                .collect(Collectors.toList());

        log.debug("Found {} metrics for campaign {} in date range", response.size(), campaignId);
        return response;
    }

    /**
     * Gets top performing campaigns for a user sorted by total conversions.
     *
     * @param userId The user ID
     * @param limit Maximum number of campaigns to return
     * @return List of top performing campaigns
     */
    public List<CampaignPerformanceResponse> getTopCampaignsByPerformance(UUID userId, int limit) {
        log.debug("Getting top {} campaigns by performance for user: {}", limit, userId);

        List<Campaign> userCampaigns = campaignRepository.findByUserId(userId);
        
        if (userCampaigns.isEmpty()) {
            log.debug("No campaigns found for user: {}", userId);
            return Collections.emptyList();
        }

        List<CampaignPerformanceResponse> campaignPerformances = new ArrayList<>();

        for (Campaign campaign : userCampaigns) {
            List<CampaignMetric> campaignMetrics = campaignMetricRepository
                    .findByCampaignId(campaign.getId());

            long totalImpressions = campaignMetrics.stream()
                    .mapToLong(CampaignMetric::getImpressions)
                    .sum();

            long totalClicks = campaignMetrics.stream()
                    .mapToLong(CampaignMetric::getClicks)
                    .sum();

            long totalConversions = campaignMetrics.stream()
                    .mapToLong(CampaignMetric::getConversions)
                    .sum();

            CampaignPerformanceResponse performance = CampaignPerformanceResponse.create(
                    campaign.getId(),
                    campaign.getName(),
                    campaign.getStatus(),
                    totalImpressions,
                    totalClicks,
                    totalConversions
            );

            campaignPerformances.add(performance);
        }

        // Sort by total conversions (descending) and limit results
        List<CampaignPerformanceResponse> topCampaigns = campaignPerformances.stream()
                .sorted(Comparator.comparingLong(CampaignPerformanceResponse::getTotalConversions).reversed())
                .limit(Math.max(1, limit)) // Ensure minimum of 1
                .collect(Collectors.toList());

        log.debug("Returning top {} campaigns for user {}", topCampaigns.size(), userId);
        return topCampaigns;
    }

    /**
     * Creates an empty dashboard summary for users with no campaigns.
     */
    private DashboardSummaryResponse createEmptyDashboardSummary() {
        return DashboardSummaryResponse.builder()
                .totalCampaigns(0)
                .activeCampaigns(0)
                .totalBudget(BigDecimal.ZERO)
                .totalImpressions(0L)
                .totalClicks(0L)
                .totalConversions(0L)
                .averageClickThroughRate(0.0)
                .averageConversionRate(0.0)
                .build();
    }
}