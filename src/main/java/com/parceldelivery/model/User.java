package com.parceldelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 20)
    @Size(min = 5, max = 20, message = "Customer ID must be between 5 and 20 characters")
    private String customerId;
    
    @NotBlank(message = "Customer name is required")
    @Size(max = 50, message = "Customer name must not exceed 50 characters")
    private String customerName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Country code is required")
    private String countryCode;
    
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    
    @NotBlank(message = "Address is required")
    @Column(length = 500)
    private String address;
    
    @NotBlank(message = "Password is required")
    @Size(max = 30, message = "Password must not exceed 30 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", 
             message = "Password must contain at least one uppercase letter, one lowercase letter, and one special character")
    private String password;
    
    @Column(length = 1000)
    private String preferences;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum Role {
        CUSTOMER, OFFICER
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (customerId == null) {
            customerId = generateCustomerId();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    private String generateCustomerId() {
        // Generate unique customer ID (format: CUST + timestamp + random)
        return "CUST" + System.currentTimeMillis() % 1000000;
    }
    
    // Constructors
    public User() {}
    
    public User(String customerName, String email, String countryCode, String mobileNumber, 
                String address, String password, String preferences) {
        this.customerName = customerName;
        this.email = email;
        this.countryCode = countryCode;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.password = password;
        this.preferences = preferences;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}