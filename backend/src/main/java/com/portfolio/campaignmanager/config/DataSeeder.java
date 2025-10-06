package com.portfolio.campaignmanager.config;

import com.portfolio.campaignmanager.model.entity.Campaign;
import com.portfolio.campaignmanager.model.entity.CampaignMetric;
import com.portfolio.campaignmanager.repository.CampaignMetricRepository;
import com.portfolio.campaignmanager.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Data seeder to generate mock campaign metrics for analytics dashboard.
 * Creates 30 days of realistic performance data for each existing campaign.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CampaignRepository campaignRepository;
    private final CampaignMetricRepository campaignMetricRepository;
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        seedCampaignMetrics();
    }

    /**
     * Seeds campaign metrics if none exist.
     * Generates 30 days of realistic performance data for each campaign.
     */
    private void seedCampaignMetrics() {
        // Check if metrics already exist
        long existingMetricsCount = campaignMetricRepository.count();
        if (existingMetricsCount > 0) {
            log.info("Campaign metrics already exist ({} records). Skipping seeding.", existingMetricsCount);
            return;
        }

        List<Campaign> campaigns = campaignRepository.findAll();
        if (campaigns.isEmpty()) {
            log.info("No campaigns found. Skipping metrics seeding.");
            return;
        }

        log.info("Starting campaign metrics seeding for {} campaigns...", campaigns.size());

        List<CampaignMetric> metricsToSave = new ArrayList<>();
        int totalMetricsCreated = 0;

        for (Campaign campaign : campaigns) {
            log.debug("Generating metrics for campaign: {} (ID: {})", campaign.getName(), campaign.getId());
            
            // Generate 30 days of data (from 30 days ago to today)
            LocalDate startDate = LocalDate.now().minusDays(30);
            LocalDate endDate = LocalDate.now();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                CampaignMetric metric = generateRealisticMetric(campaign, date);
                metricsToSave.add(metric);
                totalMetricsCreated++;
            }
        }

        // Batch save all metrics
        campaignMetricRepository.saveAll(metricsToSave);
        
        log.info("Successfully seeded {} campaign metrics for {} campaigns over 30 days", 
                totalMetricsCreated, campaigns.size());
    }

    /**
     * Generates realistic campaign metrics for a specific date.
     * Maintains realistic CTR (2-8%) and conversion rates (2-10%).
     *
     * @param campaign The campaign to generate metrics for
     * @param date The date for the metrics
     * @return A realistic CampaignMetric instance
     */
    private CampaignMetric generateRealisticMetric(Campaign campaign, LocalDate date) {
        // Base impressions: vary by campaign status and day of week
        int baseImpressions = generateBaseImpressions(campaign, date);
        
        // Generate realistic click-through rate (2-8%)
        double ctr = 0.02 + (random.nextDouble() * 0.06); // 2-8%
        int clicks = (int) Math.round(baseImpressions * ctr);
        
        // Ensure minimum clicks if impressions exist
        if (baseImpressions > 0 && clicks == 0) {
            clicks = 1;
        }
        
        // Generate realistic conversion rate (2-10% of clicks)
        double conversionRate = 0.02 + (random.nextDouble() * 0.08); // 2-10%
        int conversions = (int) Math.round(clicks * conversionRate);
        
        // Weekend effect: slightly lower performance
        int finalImpressions;
        int finalClicks;
        int finalConversions;
        
        if (date.getDayOfWeek().getValue() >= 6) { // Saturday = 6, Sunday = 7
            finalImpressions = (int) (baseImpressions * 0.8);
            finalClicks = (int) (clicks * 0.8);
            finalConversions = (int) (conversions * 0.8);
        } else {
            finalImpressions = baseImpressions;
            finalClicks = clicks;
            finalConversions = conversions;
        }

        return CampaignMetric.builder()
                .campaign(campaign)
                .date(date)
                .impressions(finalImpressions)
                .clicks(finalClicks)
                .conversions(finalConversions)
                .build();
    }

    /**
     * Generates base impressions based on campaign status and randomization.
     */
    private int generateBaseImpressions(Campaign campaign, LocalDate date) {
        int baseImpressions;
        
        switch (campaign.getStatus()) {
            case ACTIVE:
                // Active campaigns get higher traffic
                baseImpressions = 3000 + random.nextInt(7000); // 3000-10000
                break;
            case PAUSED:
                // Paused campaigns might have some residual traffic
                baseImpressions = random.nextInt(500); // 0-500
                break;
            case COMPLETED:
                // Completed campaigns might have historical data
                baseImpressions = 1000 + random.nextInt(3000); // 1000-4000
                break;
            case DRAFT:
            default:
                // Draft campaigns have minimal to no traffic
                baseImpressions = random.nextInt(100); // 0-100
                break;
        }
        
        // Add some daily variance (-20% to +20%)
        double variance = 0.8 + (random.nextDouble() * 0.4); // 0.8 to 1.2
        baseImpressions = (int) (baseImpressions * variance);
        
        return Math.max(0, baseImpressions);
    }
}