package com.parceldelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 20)
    private String paymentId;
    
    @Column(unique = true, length = 20)
    private String transactionId;
    
    @Column(unique = true, length = 20)
    private String invoiceNumber;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal transactionAmount;
    
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;
    
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @Column(name = "card_number_masked")
    private String cardNumberMasked;
    
    @Column(name = "cardholder_name")
    private String cardholderName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum TransactionType {
        CREDIT, DEBIT
    }
    
    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, REFUNDED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        transactionDate = LocalDateTime.now();
        
        if (paymentId == null) {
            paymentId = generatePaymentId();
        }
        if (transactionId == null) {
            transactionId = generateTransactionId();
        }
        if (invoiceNumber == null) {
            invoiceNumber = generateInvoiceNumber();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    private String generatePaymentId() {
        return "PAY" + System.currentTimeMillis() % 1000000000;
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() % 1000000000;
    }
    
    private String generateInvoiceNumber() {
        return "INV" + System.currentTimeMillis() % 1000000000;
    }
    
    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
    
    // Constructors
    public Payment() {}
    
    public Payment(Booking booking, BigDecimal transactionAmount, TransactionType transactionType) {
        this.booking = booking;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    
    public BigDecimal getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(BigDecimal transactionAmount) { this.transactionAmount = transactionAmount; }
    
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    
    public TransactionStatus getTransactionStatus() { return transactionStatus; }
    public void setTransactionStatus(TransactionStatus transactionStatus) { this.transactionStatus = transactionStatus; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    
    public String getCardNumberMasked() { return cardNumberMasked; }
    public void setCardNumberMasked(String cardNumberMasked) { this.cardNumberMasked = cardNumberMasked; }
    
    public String getCardholderName() { return cardholderName; }
    public void setCardholderName(String cardholderName) { this.cardholderName = cardholderName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}