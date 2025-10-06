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
 * Summary response DTO for campaign list views.
 * Contains essential campaign information for efficient list rendering.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignSummaryResponse {
    
    private UUID id;
    
    private String name;
    
    private CampaignStatus status;
    
    private BigDecimal budget;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private LocalDateTime createdAt;
}