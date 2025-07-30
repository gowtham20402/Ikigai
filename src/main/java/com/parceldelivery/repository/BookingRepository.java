package com.parceldelivery.repository;

import com.parceldelivery.model.Booking;
import com.parceldelivery.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByBookingId(String bookingId);
    
    List<Booking> findByUser(User user);
    
    Page<Booking> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    Page<Booking> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    List<Booking> findByUserAndStatus(User user, Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND " +
           "(:bookingId IS NULL OR b.bookingId LIKE %:bookingId%) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:startDate IS NULL OR b.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR b.createdAt <= :endDate) " +
           "ORDER BY b.createdAt DESC")
    Page<Booking> findBookingsWithFilters(@Param("user") User user,
                                         @Param("bookingId") String bookingId,
                                         @Param("status") Booking.BookingStatus status,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE " +
           "(:customerId IS NULL OR b.user.customerId LIKE %:customerId%) AND " +
           "(:bookingId IS NULL OR b.bookingId LIKE %:bookingId%) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:startDate IS NULL OR b.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR b.createdAt <= :endDate) " +
           "ORDER BY b.createdAt DESC")
    Page<Booking> findAllBookingsWithFilters(@Param("customerId") String customerId,
                                            @Param("bookingId") String bookingId,
                                            @Param("status") Booking.BookingStatus status,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            Pageable pageable);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.status = 'DELIVERED' AND b.user = :user")
    List<Booking> findDeliveredBookingsByUser(@Param("user") User user);
    
    @Query("SELECT b FROM Booking b WHERE b.status = 'DELIVERED'")
    List<Booking> findAllDeliveredBookings();
}