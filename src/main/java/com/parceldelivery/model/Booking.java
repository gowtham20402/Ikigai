package com.parceldelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 20)
    private String bookingId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Receiver name is required")
    private String receiverName;
    
    @NotBlank(message = "Receiver address is required")
    @Column(length = 500)
    private String receiverAddress;
    
    @NotBlank(message = "Receiver PIN is required")
    @Pattern(regexp = "\\d{6}", message = "PIN must be 6 digits")
    private String receiverPin;
    
    @NotBlank(message = "Receiver mobile is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String receiverMobile;
    
    @NotNull(message = "Parcel weight is required")
    @DecimalMin(value = "1.0", message = "Weight must be at least 1 gram")
    private Integer parcelWeightInGram;
    
    @NotBlank(message = "Parcel contents description is required")
    @Column(length = 500)
    private String parcelContentsDescription;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Delivery type is required")
    private DeliveryType parcelDeliveryType;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Packing preference is required")
    private PackingPreference parcelPackingPreference;
    
    private LocalDateTime parcelPickupTime;
    
    private LocalDateTime parcelDropoffTime;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal parcelServiceCost;
    
    private LocalDateTime parcelPaymentTime;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.NEW;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "booked_by_officer")
    private boolean bookedByOfficer = false;
    
    public enum DeliveryType {
        STANDARD("Standard Delivery", new BigDecimal("30")),
        EXPRESS("Express Delivery", new BigDecimal("80")),
        SAME_DAY("Same-Day Delivery", new BigDecimal("150"));
        
        private final String displayName;
        private final BigDecimal cost;
        
        DeliveryType(String displayName, BigDecimal cost) {
            this.displayName = displayName;
            this.cost = cost;
        }
        
        public String getDisplayName() { return displayName; }
        public BigDecimal getCost() { return cost; }
    }
    
    public enum PackingPreference {
        BASIC("Basic Packing", new BigDecimal("10")),
        PREMIUM("Premium Packing", new BigDecimal("30"));
        
        private final String displayName;
        private final BigDecimal cost;
        
        PackingPreference(String displayName, BigDecimal cost) {
            this.displayName = displayName;
            this.cost = cost;
        }
        
        public String getDisplayName() { return displayName; }
        public BigDecimal getCost() { return cost; }
    }
    
    public enum BookingStatus {
        NEW, SCHEDULED, PICKED_UP, ASSIGNED, BOOKED, IN_TRANSIT, DELIVERED, CANCELLED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bookingId == null) {
            bookingId = generateBookingId();
        }
        if (parcelServiceCost == null) {
            calculateServiceCost();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    private String generateBookingId() {
        return "BK" + System.currentTimeMillis() % 1000000000;
    }
    
    public void calculateServiceCost() {
        if (parcelWeightInGram != null && parcelDeliveryType != null && parcelPackingPreference != null) {
            BigDecimal baseRate = new BigDecimal("50");
            BigDecimal weightCharge = new BigDecimal("0.02").multiply(new BigDecimal(parcelWeightInGram));
            BigDecimal deliveryCharge = parcelDeliveryType.getCost();
            BigDecimal packingCharge = parcelPackingPreference.getCost();
            BigDecimal adminFee = bookedByOfficer ? new BigDecimal("50") : BigDecimal.ZERO;
            BigDecimal taxRate = new BigDecimal("0.05");
            
            BigDecimal subtotal = baseRate.add(weightCharge).add(deliveryCharge).add(packingCharge).add(adminFee);
            BigDecimal tax = subtotal.multiply(taxRate);
            this.parcelServiceCost = subtotal.add(tax);
        }
    }
    
    // Constructors
    public Booking() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    
    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    
    public String getReceiverPin() { return receiverPin; }
    public void setReceiverPin(String receiverPin) { this.receiverPin = receiverPin; }
    
    public String getReceiverMobile() { return receiverMobile; }
    public void setReceiverMobile(String receiverMobile) { this.receiverMobile = receiverMobile; }
    
    public Integer getParcelWeightInGram() { return parcelWeightInGram; }
    public void setParcelWeightInGram(Integer parcelWeightInGram) { 
        this.parcelWeightInGram = parcelWeightInGram;
        calculateServiceCost();
    }
    
    public String getParcelContentsDescription() { return parcelContentsDescription; }
    public void setParcelContentsDescription(String parcelContentsDescription) { 
        this.parcelContentsDescription = parcelContentsDescription; 
    }
    
    public DeliveryType getParcelDeliveryType() { return parcelDeliveryType; }
    public void setParcelDeliveryType(DeliveryType parcelDeliveryType) { 
        this.parcelDeliveryType = parcelDeliveryType;
        calculateServiceCost();
    }
    
    public PackingPreference getParcelPackingPreference() { return parcelPackingPreference; }
    public void setParcelPackingPreference(PackingPreference parcelPackingPreference) { 
        this.parcelPackingPreference = parcelPackingPreference;
        calculateServiceCost();
    }
    
    public LocalDateTime getParcelPickupTime() { return parcelPickupTime; }
    public void setParcelPickupTime(LocalDateTime parcelPickupTime) { this.parcelPickupTime = parcelPickupTime; }
    
    public LocalDateTime getParcelDropoffTime() { return parcelDropoffTime; }
    public void setParcelDropoffTime(LocalDateTime parcelDropoffTime) { this.parcelDropoffTime = parcelDropoffTime; }
    
    public BigDecimal getParcelServiceCost() { return parcelServiceCost; }
    public void setParcelServiceCost(BigDecimal parcelServiceCost) { this.parcelServiceCost = parcelServiceCost; }
    
    public LocalDateTime getParcelPaymentTime() { return parcelPaymentTime; }
    public void setParcelPaymentTime(LocalDateTime parcelPaymentTime) { this.parcelPaymentTime = parcelPaymentTime; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isBookedByOfficer() { return bookedByOfficer; }
    public void setBookedByOfficer(boolean bookedByOfficer) { 
        this.bookedByOfficer = bookedByOfficer;
        calculateServiceCost();
    }
}