import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { GuestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: '',
    loadComponent: () => import('./layouts/default-layout/default-layout.component').then(m => m.DefaultLayoutComponent),
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'campaigns',
        children: [
          {
            path: '',
            loadComponent: () => import('./features/campaigns/campaign-list/campaign-list.component').then(m => m.CampaignListComponent)
          },
          {
            path: 'create',
            loadComponent: () => import('./features/campaigns/campaign-form/campaign-form.component').then(m => m.CampaignFormComponent)
          },
          {
            path: ':id',
            loadComponent: () => import('./features/campaigns/campaign-detail/campaign-detail.component').then(m => m.CampaignDetailComponent)
          },
          {
            path: ':id/edit',
            loadComponent: () => import('./features/campaigns/campaign-form/campaign-form.component').then(m => m.CampaignFormComponent)
          }
        ]
      }
    ]
  },
  {
    path: 'auth',
    canActivate: [GuestGuard],
    children: [
      {
        path: 'login',
        loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'register',
        loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];
