import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CampaignStatus } from '../../../core/models';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge" [ngClass]="getBadgeClass()">
      {{ status }}
    </span>
  `,
  styles: [`
    .badge {
      font-size: 0.875rem;
      padding: 0.375rem 0.75rem;
      text-transform: uppercase;
      font-weight: 600;
    }
  `]
})
export class StatusBadgeComponent {
  @Input() status!: CampaignStatus;

  getBadgeClass(): string {
    switch (this.status) {
      case 'DRAFT':
        return 'bg-secondary';
      case 'ACTIVE':
        return 'bg-success';
      case 'PAUSED':
        return 'bg-warning';
      case 'COMPLETED':
        return 'bg-info';
      default:
        return 'bg-secondary';
    }
  }
}