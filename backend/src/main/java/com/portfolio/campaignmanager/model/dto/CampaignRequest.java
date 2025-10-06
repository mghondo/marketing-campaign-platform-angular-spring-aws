package com.portfolio.campaignmanager.model.dto;

import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating and updating campaigns.
 * Contains validation annotations for input validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequest {
    
    @NotBlank(message = "Campaign name is required")
    @Size(max = 200, message = "Campaign name cannot exceed 200 characters")
    private String name;
    
    private String description;
    
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @Size(max = 500, message = "Target audience description cannot exceed 500 characters")
    private String targetAudience;
    
    private CampaignStatus status;
}