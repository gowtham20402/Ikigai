package com.parceldelivery.dto;

import jakarta.validation.constraints.*;

public class LoginRequestDto {
    
    @NotBlank(message = "Customer ID is required")
    @Size(min = 5, max = 20, message = "Customer ID must be between 5 and 20 characters")
    private String customerId;
    
    @NotBlank(message = "Password is required")
    @Size(max = 30, message = "Password must not exceed 30 characters")
    private String password;
    
    // Constructors
    public LoginRequestDto() {}
    
    public LoginRequestDto(String customerId, String password) {
        this.customerId = customerId;
        this.password = password;
    }
    
    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}