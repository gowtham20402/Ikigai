package com.parceldelivery.repository;

import com.parceldelivery.model.Booking;
import com.parceldelivery.model.Feedback;
import com.parceldelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    Optional<Feedback> findByBooking(Booking booking);
    
    List<Feedback> findByUser(User user);
    
    @Query("SELECT f FROM Feedback f JOIN f.booking b WHERE b.status = 'DELIVERED' ORDER BY f.feedbackDate DESC")
    List<Feedback> findAllFeedbackForDeliveredParcels();
    
    boolean existsByBooking(Booking booking);
}