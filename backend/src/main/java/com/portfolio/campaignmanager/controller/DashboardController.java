package com.portfolio.campaignmanager.controller;

import com.portfolio.campaignmanager.exception.ResourceNotFoundException;
import com.portfolio.campaignmanager.exception.UnauthorizedException;
import com.portfolio.campaignmanager.model.dto.CampaignMetricResponse;
import com.portfolio.campaignmanager.model.dto.CampaignPerformanceResponse;
import com.portfolio.campaignmanager.model.dto.DashboardSummaryResponse;
import com.portfolio.campaignmanager.model.entity.User;
import com.portfolio.campaignmanager.repository.UserRepository;
import com.portfolio.campaignmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for dashboard analytics endpoints.
 * Provides aggregated campaign metrics and performance data.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    /**
     * Gets dashboard summary statistics for the authenticated user.
     * Aggregates data across all user campaigns and their metrics.
     *
     * @return Dashboard summary with totals and averages
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {
        UUID userId = getCurrentUserId();
        log.info("Getting dashboard summary for user: {}", userId);

        DashboardSummaryResponse summary = dashboardService.getDashboardSummary(userId);
        
        log.debug("Dashboard summary retrieved: {} campaigns, {} impressions", 
                summary.getTotalCampaigns(), summary.getTotalImpressions());
        
        return ResponseEntity.ok(summary);
    }

    /**
     * Gets metrics for a specific campaign within a date range.
     * Returns daily performance data for visualization.
     *
     * @param campaignId The campaign UUID
     * @param startDate Start date (optional, defaults to 30 days ago)
     * @param endDate End date (optional, defaults to today)
     * @return List of daily metrics for the date range
     */
    @GetMapping("/campaigns/{campaignId}/metrics")
    public ResponseEntity<List<CampaignMetricResponse>> getCampaignMetrics(
            @PathVariable UUID campaignId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        UUID userId = getCurrentUserId();
        log.info("Getting metrics for campaign: {}, user: {}, date range: {} to {}", 
                campaignId, userId, startDate, endDate);

        List<CampaignMetricResponse> metrics = dashboardService.getCampaignMetrics(
                campaignId, userId, startDate, endDate);
        
        log.debug("Retrieved {} metrics for campaign: {}", metrics.size(), campaignId);
        
        return ResponseEntity.ok(metrics);
    }

    /**
     * Gets top performing campaigns for the authenticated user.
     * Sorted by total conversions in descending order.
     *
     * @param limit Maximum number of campaigns to return (default: 5)
     * @return List of top performing campaigns
     */
    @GetMapping("/top-campaigns")
    public ResponseEntity<List<CampaignPerformanceResponse>> getTopCampaigns(
            @RequestParam(defaultValue = "5") int limit) {
        
        UUID userId = getCurrentUserId();
        log.info("Getting top {} campaigns for user: {}", limit, userId);

        // Validate limit parameter
        if (limit < 1 || limit > 50) {
            limit = 5; // Default to 5 if invalid
            log.debug("Invalid limit parameter, using default value: {}", limit);
        }

        List<CampaignPerformanceResponse> topCampaigns = dashboardService
                .getTopCampaignsByPerformance(userId, limit);
        
        log.debug("Retrieved {} top campaigns for user: {}", topCampaigns.size(), userId);
        
        return ResponseEntity.ok(topCampaigns);
    }

    /**
     * Extracts the current user ID from the security context.
     *
     * @return The UUID of the authenticated user
     * @throws UnauthorizedException if user is not authenticated
     */
    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user found");
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new UnauthorizedException("Invalid authentication principal");
        }
        
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        return user.getId();
    }
}