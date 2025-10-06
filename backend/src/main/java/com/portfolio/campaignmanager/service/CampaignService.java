package com.portfolio.campaignmanager.service;

import com.portfolio.campaignmanager.exception.ResourceNotFoundException;
import com.portfolio.campaignmanager.exception.UnauthorizedException;
import com.portfolio.campaignmanager.model.dto.CampaignRequest;
import com.portfolio.campaignmanager.model.dto.CampaignResponse;
import com.portfolio.campaignmanager.model.dto.CampaignSummaryResponse;
import com.portfolio.campaignmanager.model.entity.Campaign;
import com.portfolio.campaignmanager.model.entity.User;
import com.portfolio.campaignmanager.model.enums.CampaignStatus;
import com.portfolio.campaignmanager.repository.CampaignAssetRepository;
import com.portfolio.campaignmanager.repository.CampaignRepository;
import com.portfolio.campaignmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for campaign management operations.
 * Handles business logic for campaign CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {
    
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignAssetRepository campaignAssetRepository;
    
    /**
     * Gets the current authenticated user's ID from the security context.
     *
     * @return UUID of the current authenticated user
     * @throws UnauthorizedException if no user is authenticated
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
    
    /**
     * Creates a new campaign for the current authenticated user.
     *
     * @param request The campaign creation request
     * @return CampaignResponse containing the created campaign details
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public CampaignResponse createCampaign(CampaignRequest request) {
        UUID userId = getCurrentUserId();
        log.info("Creating new campaign for user: {}", userId);
        
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Create new campaign entity
        Campaign campaign = new Campaign();
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setBudget(request.getBudget());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setTargetAudience(request.getTargetAudience());
        campaign.setStatus(request.getStatus() != null ? request.getStatus() : CampaignStatus.DRAFT);
        campaign.setUser(user);
        
        // Save campaign
        Campaign savedCampaign = campaignRepository.save(campaign);
        
        log.info("Successfully created campaign with id: {} for user: {}", savedCampaign.getId(), userId);
        
        return convertToResponse(savedCampaign);
    }
    
    /**
     * Retrieves a campaign by its ID, ensuring the current user owns it.
     *
     * @param campaignId The ID of the campaign to retrieve
     * @return CampaignResponse containing the campaign details
     * @throws ResourceNotFoundException if the campaign is not found
     * @throws UnauthorizedException if the user doesn't own the campaign
     */
    public CampaignResponse getCampaignById(UUID campaignId) {
        UUID userId = getCurrentUserId();
        log.debug("Retrieving campaign {} for user {}", campaignId, userId);
        
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + campaignId));
        
        // Verify ownership
        if (!campaign.getUser().getId().equals(userId)) {
            log.warn("User {} attempted to access campaign {} owned by user {}", 
                    userId, campaignId, campaign.getUser().getId());
            throw new UnauthorizedException("You don't have permission to access this campaign");
        }
        
        return convertToResponse(campaign);
    }
    
    /**
     * Retrieves all campaigns for the current authenticated user.
     *
     * @return List of CampaignSummaryResponse objects
     */
    public List<CampaignSummaryResponse> getAllCampaigns() {
        UUID userId = getCurrentUserId();
        log.debug("Retrieving all campaigns for user: {}", userId);
        
        List<Campaign> campaigns = campaignRepository.findByUserId(userId);
        
        return campaigns.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves campaigns filtered by status for the current authenticated user.
     *
     * @param status The status to filter by
     * @return List of CampaignSummaryResponse objects matching the status
     */
    public List<CampaignSummaryResponse> getCampaignsByStatus(CampaignStatus status) {
        UUID userId = getCurrentUserId();
        log.debug("Retrieving campaigns with status {} for user: {}", status, userId);
        
        List<Campaign> campaigns = campaignRepository.findByUserIdAndStatus(userId, status);
        
        return campaigns.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }
    
    /**
     * Updates an existing campaign.
     *
     * @param campaignId The ID of the campaign to update
     * @param request The campaign update request
     * @return CampaignResponse containing the updated campaign details
     * @throws ResourceNotFoundException if the campaign is not found
     * @throws UnauthorizedException if the user doesn't own the campaign
     */
    @Transactional
    public CampaignResponse updateCampaign(UUID campaignId, CampaignRequest request) {
        UUID userId = getCurrentUserId();
        log.info("Updating campaign {} for user {}", campaignId, userId);
        
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + campaignId));
        
        // Verify ownership
        if (!campaign.getUser().getId().equals(userId)) {
            log.warn("User {} attempted to update campaign {} owned by user {}", 
                    userId, campaignId, campaign.getUser().getId());
            throw new UnauthorizedException("You don't have permission to update this campaign");
        }
        
        // Update campaign fields
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setBudget(request.getBudget());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setTargetAudience(request.getTargetAudience());
        if (request.getStatus() != null) {
            campaign.setStatus(request.getStatus());
        }
        
        // Save updated campaign
        Campaign updatedCampaign = campaignRepository.save(campaign);
        
        log.info("Successfully updated campaign with id: {}", campaignId);
        
        return convertToResponse(updatedCampaign);
    }
    
    /**
     * Deletes a campaign.
     *
     * @param campaignId The ID of the campaign to delete
     * @throws ResourceNotFoundException if the campaign is not found
     * @throws UnauthorizedException if the user doesn't own the campaign
     */
    @Transactional
    public void deleteCampaign(UUID campaignId) {
        UUID userId = getCurrentUserId();
        log.info("Deleting campaign {} for user {}", campaignId, userId);
        
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + campaignId));
        
        // Verify ownership
        if (!campaign.getUser().getId().equals(userId)) {
            log.warn("User {} attempted to delete campaign {} owned by user {}", 
                    userId, campaignId, campaign.getUser().getId());
            throw new UnauthorizedException("You don't have permission to delete this campaign");
        }
        
        // Delete campaign (cascade will handle assets and metrics)
        campaignRepository.delete(campaign);
        
        log.info("Successfully deleted campaign with id: {}", campaignId);
    }
    
    /**
     * Converts a Campaign entity to a CampaignResponse DTO.
     *
     * @param campaign The campaign entity to convert
     * @return CampaignResponse DTO
     */
    private CampaignResponse convertToResponse(Campaign campaign) {
        CampaignResponse response = new CampaignResponse();
        response.setId(campaign.getId());
        response.setName(campaign.getName());
        response.setDescription(campaign.getDescription());
        response.setBudget(campaign.getBudget());
        response.setStartDate(campaign.getStartDate());
        response.setEndDate(campaign.getEndDate());
        response.setTargetAudience(campaign.getTargetAudience());
        response.setStatus(campaign.getStatus());
        response.setCreatedAt(campaign.getCreatedAt());
        response.setUpdatedAt(campaign.getUpdatedAt());
        
        // Calculate asset count
        int assetCount = campaignAssetRepository.findByCampaignId(campaign.getId()).size();
        response.setAssetCount(assetCount);
        
        return response;
    }
    
    /**
     * Converts a Campaign entity to a CampaignSummaryResponse DTO.
     *
     * @param campaign The campaign entity to convert
     * @return CampaignSummaryResponse DTO
     */
    private CampaignSummaryResponse convertToSummary(Campaign campaign) {
        CampaignSummaryResponse summary = new CampaignSummaryResponse();
        summary.setId(campaign.getId());
        summary.setName(campaign.getName());
        summary.setStatus(campaign.getStatus());
        summary.setBudget(campaign.getBudget());
        summary.setStartDate(campaign.getStartDate());
        summary.setEndDate(campaign.getEndDate());
        summary.setCreatedAt(campaign.getCreatedAt());
        
        return summary;
    }
}