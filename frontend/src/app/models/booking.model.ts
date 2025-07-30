export interface Booking {
  id: number;
  bookingId: string;
  user: any;
  receiverName: string;
  receiverAddress: string;
  receiverPin: string;
  receiverMobile: string;
  parcelWeightInGram: number;
  parcelContentsDescription: string;
  parcelDeliveryType: DeliveryType;
  parcelPackingPreference: PackingPreference;
  parcelPickupTime?: string;
  parcelDropoffTime?: string;
  parcelServiceCost: number;
  parcelPaymentTime?: string;
  status: BookingStatus;
  createdAt: string;
  updatedAt: string;
  bookedByOfficer: boolean;
}

export interface BookingRequest {
  receiverName: string;
  receiverAddress: string;
  receiverPin: string;
  receiverMobile: string;
  parcelWeightInGram: number;
  parcelContentsDescription: string;
  parcelDeliveryType: DeliveryType;
  parcelPackingPreference: PackingPreference;
  parcelPickupTime?: string;
  parcelDropoffTime?: string;
  customerId?: string; // For officer booking
}

export enum DeliveryType {
  STANDARD = 'STANDARD',
  EXPRESS = 'EXPRESS',
  SAME_DAY = 'SAME_DAY'
}

export enum PackingPreference {
  BASIC = 'BASIC',
  PREMIUM = 'PREMIUM'
}

export enum BookingStatus {
  NEW = 'NEW',
  SCHEDULED = 'SCHEDULED',
  PICKED_UP = 'PICKED_UP',
  ASSIGNED = 'ASSIGNED',
  BOOKED = 'BOOKED',
  IN_TRANSIT = 'IN_TRANSIT',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export interface CostCalculation {
  totalCost: number;
  breakdown: {
    baseRate: string;
    weightCharge: string;
    deliveryCharge: number;
    packingCharge: number;
    adminFee: string;
    taxRate: string;
  };
}