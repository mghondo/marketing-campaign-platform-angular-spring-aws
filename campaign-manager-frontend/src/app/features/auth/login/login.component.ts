import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CardModule, FormModule, ButtonModule, ButtonDirective, GridModule, ContainerComponent } from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../core/services/auth.service';
import { AuthRequest } from '../../../core/models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    RouterModule,
    CardModule, 
    FormModule, 
    ButtonModule,
    ButtonDirective,
    GridModule,
    ContainerComponent,
    IconModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    // Redirect if already authenticated
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  onSubmit() {
    if (this.loginForm.valid && !this.isLoading) {
      this.isLoading = true;
      const credentials: AuthRequest = this.loginForm.value;

      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.toastr.success('Login successful!', 'Welcome');
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.isLoading = false;
          const errorMessage = error.error?.message || 'Login failed. Please check your credentials.';
          this.toastr.error(errorMessage, 'Login Failed');
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched() {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }
}