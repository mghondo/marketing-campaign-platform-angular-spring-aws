import { Component, EventEmitter, Input, Output, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface FileUploadEvent {
  file: File;
  progress?: number;
}

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="file-upload-wrapper">
      <div 
        class="file-upload-zone"
        [class.dragover]="isDragOver"
        (dragover)="onDragOver($event)"
        (dragleave)="onDragLeave($event)"
        (drop)="onDrop($event)"
        (click)="fileInput.click()">
        
        <div class="upload-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-cloud-arrow-up" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M7.646 5.146a.5.5 0 0 1 .708 0l2 2a.5.5 0 0 1-.708.708L8.5 6.707V10.5a.5.5 0 0 1-1 0V6.707L6.354 7.854a.5.5 0 1 1-.708-.708l2-2z"/>
            <path d="M4.406 3.342A5.53 5.53 0 0 1 8 2c2.69 0 4.923 2 5.166 4.579C14.758 6.804 16 8.137 16 9.773 16 11.569 14.502 13 12.687 13H3.781C1.708 13 0 11.366 0 9.318c0-1.763 1.266-3.223 2.942-3.593.143-.863.698-1.723 1.464-2.383z"/>
          </svg>
        </div>
        
        <p class="upload-text">
          <strong>Click to upload</strong> or drag and drop
        </p>
        <p class="upload-hint">
          {{ getAcceptedFormatsText() }} (Max size: {{ maxSizeMB }}MB)
        </p>
      </div>
      
      <input 
        #fileInput
        type="file"
        [accept]="accept"
        (change)="onFileSelected($event)"
        style="display: none">
      
      <div *ngIf="error" class="alert alert-danger mt-2">
        {{ error }}
      </div>
      
      <div *ngIf="uploadProgress !== null && uploadProgress < 100" class="progress mt-3">
        <div 
          class="progress-bar progress-bar-striped progress-bar-animated"
          [style.width.%]="uploadProgress"
          role="progressbar">
          {{ uploadProgress }}%
        </div>
      </div>
    </div>
  `,
  styles: [`
    .file-upload-wrapper {
      width: 100%;
    }

    .file-upload-zone {
      border: 2px dashed #dee2e6;
      border-radius: 0.375rem;
      padding: 3rem 1rem;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      background-color: #f8f9fa;
    }

    .file-upload-zone:hover {
      border-color: #0d6efd;
      background-color: #e7f1ff;
    }

    .file-upload-zone.dragover {
      border-color: #0d6efd;
      background-color: #e7f1ff;
      transform: scale(1.02);
    }

    .upload-icon {
      color: #6c757d;
      margin-bottom: 1rem;
    }

    .upload-text {
      margin-bottom: 0.5rem;
      color: #495057;
    }

    .upload-hint {
      font-size: 0.875rem;
      color: #6c757d;
      margin: 0;
    }

    .progress {
      height: 25px;
    }

    .progress-bar {
      font-size: 0.875rem;
    }
  `]
})
export class FileUploadComponent {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  @Input() accept = '.jpg,.jpeg,.png,.gif,.pdf,.mp4';
  @Input() maxSizeMB = 50;
  @Input() disabled = false;
  @Output() fileSelected = new EventEmitter<FileUploadEvent>();
  @Output() uploadProgressChange = new EventEmitter<number>();

  isDragOver = false;
  error: string | null = null;
  uploadProgress: number | null = null;

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver = false;

    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.handleFile(files[0]);
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.handleFile(input.files[0]);
    }
  }

  private handleFile(file: File) {
    this.error = null;
    this.uploadProgress = null;

    // Validate file type
    const acceptedTypes = this.accept.split(',').map(type => type.trim());
    const fileExtension = '.' + file.name.split('.').pop()?.toLowerCase();
    
    if (!acceptedTypes.some(type => fileExtension === type.toLowerCase())) {
      this.error = `Invalid file type. Please upload ${this.getAcceptedFormatsText()}`;
      return;
    }

    // Validate file size
    const maxSizeBytes = this.maxSizeMB * 1024 * 1024;
    if (file.size > maxSizeBytes) {
      this.error = `File size exceeds ${this.maxSizeMB}MB limit`;
      return;
    }

    // Emit file selected event
    this.fileSelected.emit({ file, progress: 0 });
    
    // Simulate upload progress (in real app, this would come from HTTP upload)
    this.simulateUploadProgress();
  }

  private simulateUploadProgress() {
    this.uploadProgress = 0;
    const interval = setInterval(() => {
      if (this.uploadProgress !== null) {
        this.uploadProgress += 10;
        this.uploadProgressChange.emit(this.uploadProgress);
        
        if (this.uploadProgress >= 100) {
          clearInterval(interval);
          setTimeout(() => {
            this.uploadProgress = null;
          }, 1000);
        }
      }
    }, 200);
  }

  getAcceptedFormatsText(): string {
    const formats = this.accept.split(',').map(f => f.trim().replace('.', '').toUpperCase());
    return formats.join(', ');
  }

  reset() {
    this.error = null;
    this.uploadProgress = null;
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }
}