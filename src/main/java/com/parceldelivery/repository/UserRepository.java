package com.parceldelivery.repository;

import com.parceldelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByCustomerId(String customerId);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByCustomerId(String customerId);
    
    boolean existsByEmail(String email);
}