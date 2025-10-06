package com.portfolio.campaignmanager.model.dto;

import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for campaign details.
 * Contains complete campaign information including calculated fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponse {
    
    private UUID id;
    
    private String name;
    
    private String description;
    
    private BigDecimal budget;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String targetAudience;
    
    private CampaignStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * Calculated field representing the number of assets associated with this campaign.
     * This is not stored in the database but computed at runtime.
     */
    private int assetCount;
}