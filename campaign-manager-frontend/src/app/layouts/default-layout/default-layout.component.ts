import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule } from '@angular/router';
import { IconModule } from '@coreui/icons-angular';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models';

@Component({
  selector: 'app-default-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    IconModule
  ],
  templateUrl: './default-layout.component.html',
  styleUrl: './default-layout.component.scss'
})
export class DefaultLayoutComponent {
  currentUser: User | null = null;

  constructor(private authService: AuthService) {
    this.currentUser = this.authService.getCurrentUser();
  }

  getCurrentYear(): number {
    return new Date().getFullYear();
  }

  logout(): void {
    this.authService.logout();
  }
}