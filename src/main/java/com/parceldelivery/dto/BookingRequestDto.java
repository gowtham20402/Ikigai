package com.parceldelivery.dto;

import com.parceldelivery.model.Booking;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class BookingRequestDto {
    
    @NotBlank(message = "Receiver name is required")
    private String receiverName;
    
    @NotBlank(message = "Receiver address is required")
    private String receiverAddress;
    
    @NotBlank(message = "Receiver PIN is required")
    @Pattern(regexp = "\\d{6}", message = "PIN must be 6 digits")
    private String receiverPin;
    
    @NotBlank(message = "Receiver mobile is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String receiverMobile;
    
    @NotNull(message = "Parcel weight is required")
    @Min(value = 1, message = "Weight must be at least 1 gram")
    private Integer parcelWeightInGram;
    
    @NotBlank(message = "Parcel contents description is required")
    private String parcelContentsDescription;
    
    @NotNull(message = "Delivery type is required")
    private Booking.DeliveryType parcelDeliveryType;
    
    @NotNull(message = "Packing preference is required")
    private Booking.PackingPreference parcelPackingPreference;
    
    private LocalDateTime parcelPickupTime;
    
    private LocalDateTime parcelDropoffTime;
    
    private String customerId; // For officer booking on behalf of customer
    
    // Constructors
    public BookingRequestDto() {}
    
    // Getters and Setters
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    
    public String getReceiverAddress() { return receiverAddress; }
    public void setReceiverAddress(String receiverAddress) { this.receiverAddress = receiverAddress; }
    
    public String getReceiverPin() { return receiverPin; }
    public void setReceiverPin(String receiverPin) { this.receiverPin = receiverPin; }
    
    public String getReceiverMobile() { return receiverMobile; }
    public void setReceiverMobile(String receiverMobile) { this.receiverMobile = receiverMobile; }
    
    public Integer getParcelWeightInGram() { return parcelWeightInGram; }
    public void setParcelWeightInGram(Integer parcelWeightInGram) { this.parcelWeightInGram = parcelWeightInGram; }
    
    public String getParcelContentsDescription() { return parcelContentsDescription; }
    public void setParcelContentsDescription(String parcelContentsDescription) { this.parcelContentsDescription = parcelContentsDescription; }
    
    public Booking.DeliveryType getParcelDeliveryType() { return parcelDeliveryType; }
    public void setParcelDeliveryType(Booking.DeliveryType parcelDeliveryType) { this.parcelDeliveryType = parcelDeliveryType; }
    
    public Booking.PackingPreference getParcelPackingPreference() { return parcelPackingPreference; }
    public void setParcelPackingPreference(Booking.PackingPreference parcelPackingPreference) { this.parcelPackingPreference = parcelPackingPreference; }
    
    public LocalDateTime getParcelPickupTime() { return parcelPickupTime; }
    public void setParcelPickupTime(LocalDateTime parcelPickupTime) { this.parcelPickupTime = parcelPickupTime; }
    
    public LocalDateTime getParcelDropoffTime() { return parcelDropoffTime; }
    public void setParcelDropoffTime(LocalDateTime parcelDropoffTime) { this.parcelDropoffTime = parcelDropoffTime; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}