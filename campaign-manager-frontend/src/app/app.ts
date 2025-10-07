import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ConfirmationModalComponent } from './shared/components/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ConfirmationModalComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('campaign-manager-frontend');
}
