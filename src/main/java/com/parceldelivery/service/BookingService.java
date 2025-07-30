package com.parceldelivery.service;

import com.parceldelivery.dto.BookingRequestDto;
import com.parceldelivery.model.Booking;
import com.parceldelivery.model.User;
import com.parceldelivery.repository.BookingRepository;
import com.parceldelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(BookingRequestDto bookingRequest, User user, boolean isOfficerBooking) {
        User bookingUser = user;
        
        // If officer is booking for a customer
        if (isOfficerBooking && bookingRequest.getCustomerId() != null) {
            Optional<User> customerOpt = userRepository.findByCustomerId(bookingRequest.getCustomerId());
            if (customerOpt.isPresent()) {
                bookingUser = customerOpt.get();
            } else {
                throw new RuntimeException("Customer not found with ID: " + bookingRequest.getCustomerId());
            }
        }

        Booking booking = new Booking();
        booking.setUser(bookingUser);
        booking.setReceiverName(bookingRequest.getReceiverName());
        booking.setReceiverAddress(bookingRequest.getReceiverAddress());
        booking.setReceiverPin(bookingRequest.getReceiverPin());
        booking.setReceiverMobile(bookingRequest.getReceiverMobile());
        booking.setParcelWeightInGram(bookingRequest.getParcelWeightInGram());
        booking.setParcelContentsDescription(bookingRequest.getParcelContentsDescription());
        booking.setParcelDeliveryType(bookingRequest.getParcelDeliveryType());
        booking.setParcelPackingPreference(bookingRequest.getParcelPackingPreference());
        booking.setParcelPickupTime(bookingRequest.getParcelPickupTime());
        booking.setParcelDropoffTime(bookingRequest.getParcelDropoffTime());
        booking.setBookedByOfficer(isOfficerBooking);
        booking.setStatus(Booking.BookingStatus.NEW);

        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBookingById(String bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public Page<Booking> getBookingsByUserWithPagination(User user, Pageable pageable) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public Page<Booking> getAllBookingsWithPagination(Pageable pageable) {
        return bookingRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Booking> getBookingsWithFilters(User user, String bookingId, 
                                              Booking.BookingStatus status, 
                                              LocalDateTime startDate, 
                                              LocalDateTime endDate, 
                                              Pageable pageable) {
        return bookingRepository.findBookingsWithFilters(user, bookingId, status, startDate, endDate, pageable);
    }

    public Page<Booking> getAllBookingsWithFilters(String customerId, String bookingId, 
                                                  Booking.BookingStatus status, 
                                                  LocalDateTime startDate, 
                                                  LocalDateTime endDate, 
                                                  Pageable pageable) {
        return bookingRepository.findAllBookingsWithFilters(customerId, bookingId, status, startDate, endDate, pageable);
    }

    public Booking updateBookingStatus(String bookingId, Booking.BookingStatus status) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(status);
            return bookingRepository.save(booking);
        }
        throw new RuntimeException("Booking not found with ID: " + bookingId);
    }

    public Booking updatePickupAndDropoffTime(String bookingId, LocalDateTime pickupTime, LocalDateTime dropoffTime) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setParcelPickupTime(pickupTime);
            booking.setParcelDropoffTime(dropoffTime);
            return bookingRepository.save(booking);
        }
        throw new RuntimeException("Booking not found with ID: " + bookingId);
    }

    public boolean cancelBooking(String bookingId, User user) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // Check if booking belongs to user (for customer) or allow all (for officer)
            if (user.getRole() == User.Role.CUSTOMER && !booking.getUser().equals(user)) {
                throw new RuntimeException("You can only cancel your own bookings");
            }
            
            // Check if booking can be cancelled
            if (booking.getStatus() == Booking.BookingStatus.DELIVERED || 
                booking.getStatus() == Booking.BookingStatus.IN_TRANSIT) {
                throw new RuntimeException("Cannot cancel booking that is already delivered or in transit");
            }
            
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return true;
        }
        throw new RuntimeException("Booking not found with ID: " + bookingId);
    }

    public List<Booking> getDeliveredBookingsByUser(User user) {
        return bookingRepository.findDeliveredBookingsByUser(user);
    }

    public List<Booking> getAllDeliveredBookings() {
        return bookingRepository.findAllDeliveredBookings();
    }

    public Optional<Booking> getBookingByIdForUser(String bookingId, User user) {
        Optional<Booking> bookingOpt = bookingRepository.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // For customers, only return their own bookings
            if (user.getRole() == User.Role.CUSTOMER && !booking.getUser().equals(user)) {
                return Optional.empty();
            }
            return bookingOpt;
        }
        return Optional.empty();
    }
}