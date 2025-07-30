package com.parceldelivery.controller;

import com.parceldelivery.dto.ApiResponse;
import com.parceldelivery.dto.LoginRequestDto;
import com.parceldelivery.dto.UserRegistrationDto;
import com.parceldelivery.model.User;
import com.parceldelivery.repository.UserRepository;
import com.parceldelivery.security.CustomUserDetails;
import com.parceldelivery.service.CustomUserDetailsService;
import com.parceldelivery.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            // Validate password confirmation
            if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Password and confirm password do not match"));
            }

            // Check if email already exists
            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email is already registered"));
            }

            // Create new user
            User user = new User();
            user.setCustomerName(registrationDto.getCustomerName());
            user.setEmail(registrationDto.getEmail());
            user.setCountryCode(registrationDto.getCountryCode());
            user.setMobileNumber(registrationDto.getMobileNumber());
            user.setAddress(registrationDto.getAddress());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setPreferences(registrationDto.getPreferences());
            user.setRole(User.Role.CUSTOMER);

            User savedUser = userRepository.save(user);

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("customerId", savedUser.getCustomerId());
            responseData.put("customerName", savedUser.getCustomerName());
            responseData.put("email", savedUser.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer Registration successful.", responseData));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getCustomerId(), 
                    loginRequest.getPassword()
                )
            );

            // Get user details
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            // Generate JWT token
            String token = jwtUtil.generateTokenWithRole(user.getCustomerId(), user.getRole().name());

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("customerId", user.getCustomerId());
            responseData.put("customerName", user.getCustomerName());
            responseData.put("email", user.getEmail());
            responseData.put("role", user.getRole().name());

            return ResponseEntity.ok(
                ApiResponse.success("Login successful", responseData)
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid Customer ID or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register-officer")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerOfficer(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            // Validate password confirmation
            if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Password and confirm password do not match"));
            }

            // Check if email already exists
            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email is already registered"));
            }

            // Create new officer
            User user = new User();
            user.setCustomerName(registrationDto.getCustomerName());
            user.setEmail(registrationDto.getEmail());
            user.setCountryCode(registrationDto.getCountryCode());
            user.setMobileNumber(registrationDto.getMobileNumber());
            user.setAddress(registrationDto.getAddress());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setPreferences(registrationDto.getPreferences());
            user.setRole(User.Role.OFFICER);

            User savedUser = userRepository.save(user);

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("customerId", savedUser.getCustomerId());
            responseData.put("customerName", savedUser.getCustomerName());
            responseData.put("email", savedUser.getEmail());
            responseData.put("role", savedUser.getRole().name());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Officer Registration successful.", responseData));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
}