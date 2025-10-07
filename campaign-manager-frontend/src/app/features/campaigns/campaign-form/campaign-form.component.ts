import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CardModule, GridModule, ButtonModule, ButtonDirective, ContainerComponent, FormModule } from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { Subject, takeUntil } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Campaign, CampaignRequest, CampaignStatus } from '../../../core/models';
import { CampaignService } from '../../../core/services/campaign.service';

@Component({
  selector: 'app-campaign-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    GridModule,
    ButtonModule,
    ButtonDirective,
    ContainerComponent,
    FormModule,
    IconModule
  ],
  templateUrl: './campaign-form.component.html',
  styleUrl: './campaign-form.component.scss'
})
export class CampaignFormComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  campaignForm: FormGroup;
  isEditMode = false;
  campaignId?: string;
  loading = false;
  submitting = false;

  statusOptions: { value: CampaignStatus; label: string }[] = [
    { value: 'DRAFT', label: 'Draft' },
    { value: 'ACTIVE', label: 'Active' },
    { value: 'PAUSED', label: 'Paused' },
    { value: 'COMPLETED', label: 'Completed' }
  ];

  constructor(
    private fb: FormBuilder,
    private campaignService: CampaignService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
    this.campaignForm = this.createForm();
  }

  ngOnInit() {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.campaignId = params['id'];
        this.loadCampaign();
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.maxLength(500)]],
      budget: [null, [Validators.min(0)]],
      startDate: [''],
      endDate: [''],
      targetAudience: ['', [Validators.maxLength(255)]],
      status: ['DRAFT', [Validators.required]]
    });
  }

  private loadCampaign() {
    if (!this.campaignId) return;

    this.loading = true;
    this.campaignService.getCampaignById(this.campaignId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (campaign) => {
          this.populateForm(campaign);
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

  private populateForm(campaign: Campaign) {
    this.campaignForm.patchValue({
      name: campaign.name,
      description: campaign.description,
      budget: campaign.budget,
      startDate: campaign.startDate ? campaign.startDate.split('T')[0] : '',
      endDate: campaign.endDate ? campaign.endDate.split('T')[0] : '',
      targetAudience: campaign.targetAudience,
      status: campaign.status
    });
  }

  onSubmit() {
    if (this.campaignForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.submitting = true;
    const formValue = this.campaignForm.value;
    
    const campaignData: CampaignRequest = {
      name: formValue.name,
      description: formValue.description || null,
      budget: formValue.budget || null,
      startDate: formValue.startDate || null,
      endDate: formValue.endDate || null,
      targetAudience: formValue.targetAudience || null,
      status: formValue.status
    };

    const operation = this.isEditMode && this.campaignId
      ? this.campaignService.updateCampaign(this.campaignId, campaignData)
      : this.campaignService.createCampaign(campaignData);

    operation.pipe(takeUntil(this.destroy$)).subscribe({
      next: (campaign) => {
        const message = this.isEditMode ? 'Campaign updated successfully' : 'Campaign created successfully';
        this.toastr.success(message);
        this.router.navigate(['/campaigns', campaign.id]);
        this.submitting = false;
      },
      error: (error) => {
        console.error('Error saving campaign:', error);
        const message = this.isEditMode ? 'Failed to update campaign' : 'Failed to create campaign';
        this.toastr.error(message);
        this.submitting = false;
      }
    });
  }

  onCancel() {
    this.router.navigate(['/campaigns']);
  }

  private markFormGroupTouched() {
    Object.keys(this.campaignForm.controls).forEach(key => {
      const control = this.campaignForm.get(key);
      if (control) {
        control.markAsTouched();
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.campaignForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.campaignForm.get(fieldName);
    if (!field || !field.errors) return '';

    const errors = field.errors;
    if (errors['required']) return `${this.getFieldLabel(fieldName)} is required`;
    if (errors['minlength']) return `${this.getFieldLabel(fieldName)} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) return `${this.getFieldLabel(fieldName)} must not exceed ${errors['maxlength'].requiredLength} characters`;
    if (errors['min']) return `${this.getFieldLabel(fieldName)} must be at least ${errors['min'].min}`;

    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      name: 'Campaign name',
      description: 'Description',
      budget: 'Budget',
      startDate: 'Start date',
      endDate: 'End date',
      targetAudience: 'Target audience',
      status: 'Status'
    };
    return labels[fieldName] || fieldName;
  }

  get pageTitle(): string {
    return this.isEditMode ? 'Edit Campaign' : 'Create New Campaign';
  }

  get submitButtonText(): string {
    if (this.submitting) {
      return this.isEditMode ? 'Updating...' : 'Creating...';
    }
    return this.isEditMode ? 'Update Campaign' : 'Create Campaign';
  }
}