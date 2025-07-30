import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Redirect if already logged in
    if (this.authService.isLoggedIn) {
      this.redirectToDashboard();
      return;
    }

    this.loginForm = this.formBuilder.group({
      customerId: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.maxLength(30)]]
    });
  }

  get f() { 
    return this.loginForm.controls; 
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    const loginData = {
      customerId: this.f['customerId'].value,
      password: this.f['password'].value
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success) {
          this.redirectToDashboard();
        } else {
          this.error = response.message;
        }
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Login failed. Please try again.';
      }
    });
  }

  private redirectToDashboard(): void {
    const userRole = this.authService.userRole;
    if (userRole === 'CUSTOMER') {
      this.router.navigate(['/customer']);
    } else if (userRole === 'OFFICER') {
      this.router.navigate(['/officer']);
    }
  }
}