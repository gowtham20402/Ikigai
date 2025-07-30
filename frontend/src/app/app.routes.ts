import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { 
    path: 'login', 
    loadComponent: () => import('./components/auth/login/login.component').then(m => m.LoginComponent)
  },
  { 
    path: 'register', 
    loadComponent: () => import('./components/auth/register/register.component').then(m => m.RegisterComponent)
  },
  { 
    path: 'customer', 
    loadComponent: () => import('./components/dashboard/customer-dashboard/customer-dashboard.component').then(m => m.CustomerDashboardComponent),
    canActivate: [authGuard],
    data: { role: 'CUSTOMER' }
  },
  { 
    path: 'officer', 
    loadComponent: () => import('./components/dashboard/officer-dashboard/officer-dashboard.component').then(m => m.OfficerDashboardComponent),
    canActivate: [authGuard],
    data: { role: 'OFFICER' }
  },
  { 
    path: 'customer/booking', 
    loadComponent: () => import('./components/booking/customer-booking/customer-booking.component').then(m => m.CustomerBookingComponent),
    canActivate: [authGuard],
    data: { role: 'CUSTOMER' }
  },
  { 
    path: 'customer/bookings', 
    loadComponent: () => import('./components/booking/booking-list/booking-list.component').then(m => m.BookingListComponent),
    canActivate: [authGuard],
    data: { role: 'CUSTOMER' }
  },
  { 
    path: 'customer/tracking', 
    loadComponent: () => import('./components/tracking/tracking.component').then(m => m.TrackingComponent),
    canActivate: [authGuard],
    data: { role: 'CUSTOMER' }
  },
  { 
    path: 'officer/booking', 
    loadComponent: () => import('./components/booking/officer-booking/officer-booking.component').then(m => m.OfficerBookingComponent),
    canActivate: [authGuard],
    data: { role: 'OFFICER' }
  },
  { 
    path: 'officer/bookings', 
    loadComponent: () => import('./components/booking/all-bookings/all-bookings.component').then(m => m.AllBookingsComponent),
    canActivate: [authGuard],
    data: { role: 'OFFICER' }
  },
  { 
    path: 'officer/tracking', 
    loadComponent: () => import('./components/tracking/officer-tracking/officer-tracking.component').then(m => m.OfficerTrackingComponent),
    canActivate: [authGuard],
    data: { role: 'OFFICER' }
  },
  { path: '**', redirectTo: '/login' }
];