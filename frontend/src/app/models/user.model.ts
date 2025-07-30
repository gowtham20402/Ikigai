export interface User {
  customerId: string;
  customerName: string;
  email: string;
  countryCode: string;
  mobileNumber: string;
  address: string;
  preferences?: string;
  role: 'CUSTOMER' | 'OFFICER';
  createdAt: string;
}

export interface UserRegistration {
  customerName: string;
  email: string;
  countryCode: string;
  mobileNumber: string;
  address: string;
  password: string;
  confirmPassword: string;
  preferences?: string;
}

export interface LoginRequest {
  customerId: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  customerId: string;
  customerName: string;
  email: string;
  role: string;
}