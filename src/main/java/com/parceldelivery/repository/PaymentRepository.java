package com.parceldelivery.repository;

import com.parceldelivery.model.Booking;
import com.parceldelivery.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByPaymentId(String paymentId);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByBooking(Booking booking);
    
    Optional<Payment> findByInvoiceNumber(String invoiceNumber);
}