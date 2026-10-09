export type Role = 'ROLE_USER' | 'ROLE_ADMIN';

export interface User {
  id: number;
  fullName: string;
  email: string;
  phone: string;
  profilePhoto?: string;
  isActive: boolean;
  roles: Role[];
  createdAt?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  email: string;
  fullName: string;
  phone: string;
  profilePhoto?: string;
  roles: Role[];
}

export type VehicleType = 'CAR' | 'BIKE' | 'SUV' | 'SEDAN' | 'HATCHBACK' | 'LUXURY' | 'VAN';
export type VehicleStatus = 'AVAILABLE' | 'BOOKED' | 'RENTED' | 'MAINTENANCE' | 'OUT_OF_SERVICE';

export interface Vehicle {
  id: number;
  brand: string;
  model: string;
  year: number;
  vehicleType: VehicleType;
  registrationNumber: string;
  description: string;
  pricePerDay: number;
  securityDeposit: number;
  fuelType: string;
  transmission: string;
  seatingCapacity: number;
  location: string;
  status: VehicleStatus;
  rating: number;
  totalReviews: number;
  featured: boolean;
  primaryImageUrl: string;
  imageUrls: string[];
}

export interface VehicleDetails {
  vehicle: Vehicle;
  recentReviews: Review[];
  isAvailableNow: boolean;
  activeBookingsCount: number;
}

export type BookingStatus = 'PENDING' | 'CONFIRMED' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED' | 'EXTENDED';

export interface Booking {
  id: number;
  bookingReference: string;
  bookingNumber: string;
  userId: number;
  userName?: string;
  userFullName?: string;
  userEmail: string;
  userPhone: string;
  vehicleId: number;
  vehicleBrand: string;
  vehicleModel: string;
  vehicleType: string;
  vehicleRegistrationNumber: string;
  vehicleImageUrl?: string;
  pickupDate: string;
  returnDate: string;
  pickupLocation: string;
  returnLocation: string;
  baseAmount: number;
  discountAmount: number;
  taxAmount: number;
  securityDeposit: number;
  totalAmount: number;
  finalAmount: number;
  status: BookingStatus;
  couponCode?: string;
  cancellationReason?: string;
  isPaid: boolean;
  paymentStatus: string;
  createdAt: string;
  updatedAt?: string;
}

export interface BookingQuote {
  vehicleId: number;
  vehicleName: string;
  rentalDays: number;
  pricePerDay: number;
  baseAmount: number;
  discountAmount: number;
  couponCode?: string;
  taxAmount: number;
  securityDeposit: number;
  totalAmount: number;
}

export type PaymentMethod = 'CARD' | 'UPI' | 'CASH';
export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';

export interface Payment {
  id: number;
  bookingId: number;
  bookingReference: string;
  amount: number;
  paymentMethod: PaymentMethod;
  status: PaymentStatus;
  transactionReference: string;
  demoNote: string;
  createdAt: string;
}

export type DiscountType = 'PERCENTAGE' | 'FIXED';

export interface Coupon {
  id: number;
  code: string;
  discountType: DiscountType;
  discountValue: number;
  minimumBookingAmount: number;
  maximumDiscount?: number;
  startDate: string;
  expiryDate: string;
  usageLimit: number;
  timesUsed: number;
  active: boolean;
  isValid?: boolean;
  message?: string;
}

export interface Review {
  id: number;
  vehicleId: number;
  vehicleName: string;
  userId: number;
  userName: string;
  userPhoto?: string;
  bookingId: number;
  rating: number;
  comment: string;
  isHidden: boolean;
  createdAt: string;
}

export interface NotificationItem {
  id: number;
  title: string;
  message: string;
  type: string;
  isRead: boolean;
  createdAt: string;
}

export type MaintenanceType = 'SERVICE' | 'REPAIR' | 'OIL_CHANGE' | 'TYRE_CHANGE' | 'INSPECTION' | 'OTHER';
export type MaintenanceStatus = 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED';

export interface MaintenanceRecord {
  id: number;
  vehicleId: number;
  vehicleBrand: string;
  vehicleModel: string;
  vehicleRegistrationNumber: string;
  maintenanceType: MaintenanceType;
  description: string;
  cost: number;
  startDate: string;
  endDate?: string;
  status: MaintenanceStatus;
  notes?: string;
  createdAt: string;
}

export interface DashboardStats {
  totalUsers: number;
  totalVehicles: number;
  activeRentals: number;
  upcomingBookings: number;
  completedRentals: number;
  cancelledRentals: number;
  totalRevenue: number;
  vehiclesUnderMaintenance: number;
}

export interface DashboardAnalytics {
  revenueByMonth: { month: string; revenue: number }[];
  categoryDistribution: { category: string; count: number }[];
  mostRentedVehicles: { vehicle: string; rentalCount: number }[];
  bookingStatusDistribution: Record<string, number>;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  last: boolean;
  first: boolean;
}
