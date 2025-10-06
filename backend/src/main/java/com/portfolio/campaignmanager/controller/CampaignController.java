package com.portfolio.campaignmanager.controller;

import com.portfolio.campaignmanager.model.dto.CampaignRequest;
import com.portfolio.campaignmanager.model.dto.CampaignResponse;
import com.portfolio.campaignmanager.model.dto.CampaignSummaryResponse;
import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import com.portfolio.campaignmanager.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for campaign management operations.
 * Provides endpoints for CRUD operations on campaigns.
 */
@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {
    
    private final CampaignService campaignService;
    
    /**
     * Creates a new campaign.
     *
     * @param request The campaign creation request
     * @return ResponseEntity containing the created campaign details
     */
    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(@Valid @RequestBody CampaignRequest request) {
        log.info("Creating new campaign: {}", request.getName());
        
        CampaignResponse response = campaignService.createCampaign(request);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Retrieves all campaigns for the user, optionally filtered by status.
     *
     * @param status Optional status filter
     * @return ResponseEntity containing the list of campaigns
     */
    @GetMapping
    public ResponseEntity<List<CampaignSummaryResponse>> getCampaigns(
            @RequestParam(required = false) CampaignStatus status) {
        
        log.debug("Retrieving campaigns with status filter: {}", status);
        
        List<CampaignSummaryResponse> campaigns;
        
        if (status != null) {
            campaigns = campaignService.getCampaignsByStatus(status);
        } else {
            campaigns = campaignService.getAllCampaigns();
        }
        
        return new ResponseEntity<>(campaigns, HttpStatus.OK);
    }
    
    /**
     * Retrieves a specific campaign by its ID.
     *
     * @param id The ID of the campaign to retrieve
     * @return ResponseEntity containing the campaign details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable UUID id) {
        log.debug("Retrieving campaign with id: {}", id);
        
        CampaignResponse response = campaignService.getCampaignById(id);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Updates an existing campaign.
     *
     * @param id The ID of the campaign to update
     * @param request The campaign update request
     * @return ResponseEntity containing the updated campaign details
     */
    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(
            @PathVariable UUID id,
            @Valid @RequestBody CampaignRequest request) {
        
        log.info("Updating campaign with id: {}", id);
        
        CampaignResponse response = campaignService.updateCampaign(id, request);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Deletes a campaign.
     *
     * @param id The ID of the campaign to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable UUID id) {
        log.info("Deleting campaign with id: {}", id);
        
        campaignService.deleteCampaign(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}