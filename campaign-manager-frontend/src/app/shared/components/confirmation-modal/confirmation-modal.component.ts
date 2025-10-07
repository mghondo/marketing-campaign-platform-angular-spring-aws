import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationService, ConfirmationConfig } from '../../../core/services/confirmation.service';

@Component({
  selector: 'app-confirmation-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal fade" [class.show]="isVisible" [style.display]="isVisible ? 'block' : 'none'" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content" *ngIf="config">
          <div class="modal-header">
            <h5 class="modal-title">{{ config.title }}</h5>
            <button type="button" class="btn-close" (click)="onCancel()"></button>
          </div>
          <div class="modal-body">
            <p>{{ config.message }}</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" (click)="onCancel()">
              {{ config.cancelText }}
            </button>
            <button type="button" class="btn" [ngClass]="config.confirmClass" (click)="onConfirm()">
              {{ config.confirmText }}
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal-backdrop fade" [class.show]="isVisible" *ngIf="isVisible"></div>
  `,
  styles: [`
    .modal {
      z-index: 1050;
    }
    .modal-backdrop {
      z-index: 1040;
    }
  `]
})
export class ConfirmationModalComponent implements OnInit {
  isVisible = false;
  config: ConfirmationConfig | null = null;

  constructor(private confirmationService: ConfirmationService) {}

  ngOnInit() {
    this.confirmationService.confirmation$.subscribe(config => {
      this.config = config;
      this.isVisible = true;
    });
  }

  onConfirm() {
    this.isVisible = false;
    this.confirmationService.handleResult(true);
  }

  onCancel() {
    this.isVisible = false;
    this.confirmationService.handleResult(false);
  }
}