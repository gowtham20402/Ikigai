import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { ApiResponse } from '../models/api-response.model';
import { AuthResponse, LoginRequest, UserRegistration } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    // Load user from localStorage on service initialization
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  get currentUserValue(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUserValue && !!this.getToken();
  }

  get userRole(): string | null {
    return this.currentUserValue?.role || null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  register(userData: UserRegistration): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/register`, userData);
  }

  registerOfficer(userData: UserRegistration): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/register-officer`, userData);
  }

  login(credentials: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            const authData = response.data;
            localStorage.setItem('token', authData.token);
            localStorage.setItem('currentUser', JSON.stringify(authData));
            this.currentUserSubject.next(authData);
          }
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isCustomer(): boolean {
    return this.userRole === 'CUSTOMER';
  }

  isOfficer(): boolean {
    return this.userRole === 'OFFICER';
  }
}