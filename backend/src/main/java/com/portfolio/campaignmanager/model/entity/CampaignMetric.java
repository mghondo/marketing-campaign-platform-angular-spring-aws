package com.portfolio.campaignmanager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "campaign_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignMetric {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Campaign campaign;
    
    @Column(nullable = false)
    private Integer impressions = 0;
    
    @Column(nullable = false)
    private Integer clicks = 0;
    
    @Column(nullable = false)
    private Integer conversions = 0;
    
    @Column(nullable = false)
    private LocalDate date;
}