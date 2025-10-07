import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CardModule, GridModule, TableModule, ButtonModule, ButtonDirective, ContainerComponent, FormModule } from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { Subject, takeUntil } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Campaign, CampaignStatus } from '../../../core/models';
import { CampaignService } from '../../../core/services/campaign.service';
import { ConfirmationService } from '../../../core/services/confirmation.service';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';

@Component({
  selector: 'app-campaign-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    GridModule,
    TableModule,
    ButtonModule,
    ButtonDirective,
    ContainerComponent,
    FormModule,
    IconModule,
    StatusBadgeComponent
  ],
  templateUrl: './campaign-list.component.html',
  styleUrl: './campaign-list.component.scss'
})
export class CampaignListComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  campaigns: Campaign[] = [];
  filteredCampaigns: Campaign[] = [];
  loading = true;
  searchTerm = '';
  statusFilter: CampaignStatus | 'ALL' = 'ALL';

  statusOptions = [
    { value: 'ALL', label: 'All Statuses' },
    { value: 'DRAFT', label: 'Draft' },
    { value: 'ACTIVE', label: 'Active' },
    { value: 'PAUSED', label: 'Paused' },
    { value: 'COMPLETED', label: 'Completed' }
  ];

  constructor(
    private campaignService: CampaignService,
    private confirmationService: ConfirmationService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadCampaigns();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCampaigns() {
    this.loading = true;
    
    const status = this.statusFilter === 'ALL' ? undefined : this.statusFilter;
    
    this.campaignService.getCampaigns(status)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (campaigns) => {
          this.campaigns = campaigns;
          this.applyFilters();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading campaigns:', error);
          this.toastr.error('Failed to load campaigns');
          this.loading = false;
        }
      });
  }

  applyFilters() {
    this.filteredCampaigns = this.campaigns.filter(campaign => {
      const matchesSearch = !this.searchTerm || 
        campaign.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        (campaign.description && campaign.description.toLowerCase().includes(this.searchTerm.toLowerCase()));
      
      const matchesStatus = this.statusFilter === 'ALL' || campaign.status === this.statusFilter;
      
      return matchesSearch && matchesStatus;
    });
  }

  onSearchChange() {
    this.applyFilters();
  }

  onStatusFilterChange() {
    this.applyFilters();
  }

  viewCampaign(campaign: Campaign) {
    this.router.navigate(['/campaigns', campaign.id]);
  }

  editCampaign(campaign: Campaign) {
    this.router.navigate(['/campaigns', campaign.id, 'edit']);
  }

  deleteCampaign(campaign: Campaign) {
    this.confirmationService.confirm(
      `Are you sure you want to delete the campaign "${campaign.name}"? This action cannot be undone.`,
      'Delete Campaign'
    )
    .pipe(takeUntil(this.destroy$))
    .subscribe(confirmed => {
      if (confirmed) {
        this.campaignService.deleteCampaign(campaign.id)
          .pipe(takeUntil(this.destroy$))
          .subscribe({
            next: () => {
              this.toastr.success('Campaign deleted successfully');
              this.loadCampaigns();
            },
            error: (error) => {
              console.error('Error deleting campaign:', error);
              this.toastr.error('Failed to delete campaign');
            }
          });
      }
    });
  }

  createCampaign() {
    this.router.navigate(['/campaigns/create']);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }

  formatBudget(budget: number | null): string {
    if (!budget) return 'Not set';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(budget);
  }
}