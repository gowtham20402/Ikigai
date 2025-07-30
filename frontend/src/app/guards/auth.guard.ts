import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRole = route.data['role'];
  const userRole = authService.userRole;

  if (requiredRole && userRole !== requiredRole) {
    // Redirect to appropriate dashboard based on user role
    if (userRole === 'CUSTOMER') {
      router.navigate(['/customer']);
    } else if (userRole === 'OFFICER') {
      router.navigate(['/officer']);
    } else {
      router.navigate(['/login']);
    }
    return false;
  }

  return true;
};