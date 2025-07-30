package com.parceldelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Feedback description is required")
    @Column(length = 1000)
    private String feedbackDescription;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
    
    @Column(name = "feedback_date")
    private LocalDateTime feedbackDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        feedbackDate = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Feedback() {}
    
    public Feedback(Booking booking, User user, String feedbackDescription, Integer rating) {
        this.booking = booking;
        this.user = user;
        this.feedbackDescription = feedbackDescription;
        this.rating = rating;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getFeedbackDescription() { return feedbackDescription; }
    public void setFeedbackDescription(String feedbackDescription) { this.feedbackDescription = feedbackDescription; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public LocalDateTime getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDateTime feedbackDate) { this.feedbackDate = feedbackDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}