package com.parceldelivery.service;

import com.parceldelivery.model.User;
import com.parceldelivery.repository.UserRepository;
import com.parceldelivery.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        User user = userRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with customer ID: " + customerId));
        
        return new CustomUserDetails(user);
    }
}