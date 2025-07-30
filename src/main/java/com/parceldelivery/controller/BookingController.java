package com.parceldelivery.controller;

import com.parceldelivery.dto.ApiResponse;
import com.parceldelivery.dto.BookingRequestDto;
import com.parceldelivery.model.Booking;
import com.parceldelivery.model.User;
import com.parceldelivery.security.CustomUserDetails;
import com.parceldelivery.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Customer booking endpoints
    @PostMapping("/api/customer/bookings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createCustomerBooking(
            @Valid @RequestBody BookingRequestDto bookingRequest,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            Booking booking = bookingService.createBooking(bookingRequest, user, false);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookingId", booking.getBookingId());
            responseData.put("customerName", user.getCustomerName());
            responseData.put("address", user.getAddress());
            responseData.put("contactDetails", user.getCountryCode() + user.getMobileNumber());
            responseData.put("receiverName", booking.getReceiverName());
            responseData.put("receiverAddress", booking.getReceiverAddress());
            responseData.put("serviceCost", booking.getParcelServiceCost());
            responseData.put("status", booking.getStatus());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully. Please proceed to payment.", responseData));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Booking failed: " + e.getMessage()));
        }
    }

    // Officer booking endpoints
    @PostMapping("/api/officer/bookings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createOfficerBooking(
            @Valid @RequestBody BookingRequestDto bookingRequest,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User officer = userDetails.getUser();

            Booking booking = bookingService.createBooking(bookingRequest, officer, true);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookingId", booking.getBookingId());
            responseData.put("customerName", booking.getUser().getCustomerName());
            responseData.put("address", booking.getUser().getAddress());
            responseData.put("contactDetails", booking.getUser().getCountryCode() + booking.getUser().getMobileNumber());
            responseData.put("receiverName", booking.getReceiverName());
            responseData.put("receiverAddress", booking.getReceiverAddress());
            responseData.put("serviceCost", booking.getParcelServiceCost());
            responseData.put("status", booking.getStatus());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully. Payment to be collected at office.", responseData));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Booking failed: " + e.getMessage()));
        }
    }

    // Common endpoints for both customer and officer
    @GetMapping("/api/common/bookings/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(
            @PathVariable String bookingId,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            Optional<Booking> booking = bookingService.getBookingByIdForUser(bookingId, user);
            if (booking.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Booking found", booking.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Booking not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving booking: " + e.getMessage()));
        }
    }

    // Customer specific endpoints
    @GetMapping("/api/customer/bookings")
    public ResponseEntity<ApiResponse<Page<Booking>>> getCustomerBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) Booking.BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            Pageable pageable = PageRequest.of(page, size);
            Page<Booking> bookings = bookingService.getBookingsWithFilters(user, bookingId, status, startDate, endDate, pageable);

            return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving bookings: " + e.getMessage()));
        }
    }

    @PostMapping("/api/customer/bookings/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelCustomerBooking(
            @PathVariable String bookingId,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            bookingService.cancelBooking(bookingId, user);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Booking cancel failed: " + e.getMessage()));
        }
    }

    // Officer specific endpoints
    @GetMapping("/api/officer/bookings")
    public ResponseEntity<ApiResponse<Page<Booking>>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) Booking.BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Booking> bookings = bookingService.getAllBookingsWithFilters(customerId, bookingId, status, startDate, endDate, pageable);

            return ResponseEntity.ok(ApiResponse.success("All bookings retrieved successfully", bookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error retrieving bookings: " + e.getMessage()));
        }
    }

    @PutMapping("/api/officer/bookings/{bookingId}/status")
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @PathVariable String bookingId,
            @RequestParam Booking.BookingStatus status) {
        try {
            Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status);
            return ResponseEntity.ok(ApiResponse.success("Booking status updated successfully", updatedBooking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Status update failed: " + e.getMessage()));
        }
    }

    @PutMapping("/api/officer/bookings/{bookingId}/schedule")
    public ResponseEntity<ApiResponse<Booking>> updatePickupSchedule(
            @PathVariable String bookingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dropoffTime) {
        try {
            Booking updatedBooking = bookingService.updatePickupAndDropoffTime(bookingId, pickupTime, dropoffTime);
            return ResponseEntity.ok(ApiResponse.success("Pickup schedule updated successfully", updatedBooking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Schedule update failed: " + e.getMessage()));
        }
    }

    @PostMapping("/api/officer/bookings/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOfficerBooking(
            @PathVariable String bookingId,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            bookingService.cancelBooking(bookingId, user);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully and Booking Amount will be refunded to the customer account within 5 working days"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Booking cancel failed"));
        }
    }

    // Cost calculation endpoint
    @PostMapping("/api/common/calculate-cost")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateCost(
            @RequestParam Integer weight,
            @RequestParam Booking.DeliveryType deliveryType,
            @RequestParam Booking.PackingPreference packingPreference,
            @RequestParam(defaultValue = "false") boolean isOfficerBooking) {
        try {
            // Create a temporary booking to calculate cost
            Booking tempBooking = new Booking();
            tempBooking.setParcelWeightInGram(weight);
            tempBooking.setParcelDeliveryType(deliveryType);
            tempBooking.setParcelPackingPreference(packingPreference);
            tempBooking.setBookedByOfficer(isOfficerBooking);
            tempBooking.calculateServiceCost();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("totalCost", tempBooking.getParcelServiceCost());
            responseData.put("breakdown", Map.of(
                "baseRate", "50",
                "weightCharge", "0.02 × " + weight,
                "deliveryCharge", deliveryType.getCost(),
                "packingCharge", packingPreference.getCost(),
                "adminFee", isOfficerBooking ? "50" : "0",
                "taxRate", "5%"
            ));

            return ResponseEntity.ok(ApiResponse.success("Cost calculated successfully", responseData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Cost calculation failed: " + e.getMessage()));
        }
    }
}