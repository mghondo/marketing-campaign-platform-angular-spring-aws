import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { CardModule, GridModule, ButtonModule, ButtonDirective, ContainerComponent, TableModule } from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { Subject, takeUntil } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Campaign, CampaignAsset } from '../../../core/models';
import { CampaignService } from '../../../core/services/campaign.service';
import { ConfirmationService } from '../../../core/services/confirmation.service';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';
import { FileUploadComponent, FileUploadEvent } from '../../../shared/components/file-upload/file-upload.component';

@Component({
  selector: 'app-campaign-detail',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    GridModule,
    ButtonModule,
    ButtonDirective,
    ContainerComponent,
    TableModule,
    IconModule,
    StatusBadgeComponent,
    FileUploadComponent
  ],
  templateUrl: './campaign-detail.component.html',
  styleUrl: './campaign-detail.component.scss'
})
export class CampaignDetailComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  campaign: Campaign | null = null;
  assets: CampaignAsset[] = [];
  loading = true;
  assetsLoading = false;
  uploadingAsset = false;
  campaignId!: string;

  constructor(
    private campaignService: CampaignService,
    private confirmationService: ConfirmationService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      this.campaignId = params['id'];
      if (this.campaignId) {
        this.loadCampaign();
        this.loadAssets();
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadCampaign() {
    this.loading = true;
    this.campaignService.getCampaignById(this.campaignId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (campaign) => {
          this.campaign = campaign;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading campaign:', error);
          this.toastr.error('Failed to load campaign');
          this.router.navigate(['/campaigns']);
          this.loading = false;
        }
      });
  }

  loadAssets() {
    this.assetsLoading = true;
    this.campaignService.getAssets(this.campaignId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (assets) => {
          this.assets = assets;
          this.assetsLoading = false;
        },
        error: (error) => {
          console.error('Error loading assets:', error);
          this.toastr.error('Failed to load campaign assets');
          this.assetsLoading = false;
        }
      });
  }

  onFileSelected(event: FileUploadEvent) {
    this.uploadingAsset = true;
    this.campaignService.uploadAsset(this.campaignId, event.file)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (asset) => {
          this.toastr.success('Asset uploaded successfully');
          this.assets.push(asset);
          this.updateCampaignAssetCount();
          this.uploadingAsset = false;
        },
        error: (error) => {
          console.error('Error uploading asset:', error);
          this.toastr.error('Failed to upload asset');
          this.uploadingAsset = false;
        }
      });
  }

  deleteAsset(asset: CampaignAsset) {
    this.confirmationService.confirm(
      `Are you sure you want to delete "${asset.fileName}"? This action cannot be undone.`,
      'Delete Asset'
    )
    .pipe(takeUntil(this.destroy$))
    .subscribe(confirmed => {
      if (confirmed) {
        this.campaignService.deleteAsset(asset.id)
          .pipe(takeUntil(this.destroy$))
          .subscribe({
            next: () => {
              this.toastr.success('Asset deleted successfully');
              this.assets = this.assets.filter(a => a.id !== asset.id);
              this.updateCampaignAssetCount();
            },
            error: (error) => {
              console.error('Error deleting asset:', error);
              this.toastr.error('Failed to delete asset');
            }
          });
      }
    });
  }

  private updateCampaignAssetCount() {
    if (this.campaign) {
      this.campaign.assetCount = this.assets.length;
    }
  }

  editCampaign() {
    this.router.navigate(['/campaigns', this.campaignId, 'edit']);
  }

  backToCampaigns() {
    this.router.navigate(['/campaigns']);
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

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  getFileTypeIcon(fileName: string): string {
    const extension = fileName.split('.').pop()?.toLowerCase();
    
    switch (extension) {
      case 'jpg':
      case 'jpeg':
      case 'png':
      case 'gif':
      case 'webp':
        return 'cilImageBroken';
      case 'mp4':
      case 'mov':
      case 'avi':
      case 'wmv':
        return 'cilVideo';
      case 'pdf':
        return 'cilDescription';
      case 'doc':
      case 'docx':
        return 'cilDescription';
      case 'xls':
      case 'xlsx':
        return 'cilSpreadsheet';
      default:
        return 'cilFile';
    }
  }

  isImageFile(fileName: string): boolean {
    const extension = fileName.split('.').pop()?.toLowerCase();
    return ['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(extension || '');
  }
}