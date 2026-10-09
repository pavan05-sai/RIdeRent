package com.vehiclerental.dto.response;

import com.vehiclerental.entity.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private String bookingReference;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Long vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleType;
    private String vehicleRegistrationNumber;
    private String vehicleImageUrl;
    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private String pickupLocation;
    private String returnLocation;
    private BigDecimal baseAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal securityDeposit;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String couponCode;
    private String cancellationReason;
    private Boolean isPaid;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookingResponse() {
    }

    public BookingResponse(Long id, String bookingReference, Long userId, String userName, String userEmail, String userPhone, Long vehicleId, String vehicleBrand, String vehicleModel, String vehicleType, String vehicleRegistrationNumber, String vehicleImageUrl, LocalDateTime pickupDate, LocalDateTime returnDate, String pickupLocation, String returnLocation, BigDecimal baseAmount, BigDecimal discountAmount, BigDecimal taxAmount, BigDecimal securityDeposit, BigDecimal totalAmount, BookingStatus status, String couponCode, String cancellationReason, Boolean isPaid, String paymentStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.vehicleId = vehicleId;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.vehicleImageUrl = vehicleImageUrl;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.baseAmount = baseAmount;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.securityDeposit = securityDeposit;
        this.totalAmount = totalAmount;
        this.status = status;
        this.couponCode = couponCode;
        this.cancellationReason = cancellationReason;
        this.isPaid = isPaid;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return this.bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleBrand() {
        return this.vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return this.vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleRegistrationNumber() {
        return this.vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getVehicleImageUrl() {
        return this.vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public LocalDateTime getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDateTime getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getPickupLocation() {
        return this.pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getReturnLocation() {
        return this.returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public BigDecimal getBaseAmount() {
        return this.baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getSecurityDeposit() {
        return this.securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getBookingNumber() {
        return this.bookingReference;
    }

    public BigDecimal getFinalAmount() {
        return this.totalAmount;
    }

    public String getUserFullName() {
        return this.userName;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getCouponCode() {
        return this.couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Boolean getIsPaid() {
        return this.isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
    public Boolean getPaid() {
        return this.isPaid;
    }
    public void setPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String bookingReference;
        private Long userId;
        private String userName;
        private String userEmail;
        private String userPhone;
        private Long vehicleId;
        private String vehicleBrand;
        private String vehicleModel;
        private String vehicleType;
        private String vehicleRegistrationNumber;
        private String vehicleImageUrl;
        private LocalDateTime pickupDate;
        private LocalDateTime returnDate;
        private String pickupLocation;
        private String returnLocation;
        private BigDecimal baseAmount;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal securityDeposit;
        private BigDecimal totalAmount;
        private BookingStatus status;
        private String couponCode;
        private String cancellationReason;
        private Boolean isPaid;
        private String paymentStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder bookingReference(String bookingReference) {
            this.bookingReference = bookingReference;
            return this;
        }
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }
        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }
        public Builder userPhone(String userPhone) {
            this.userPhone = userPhone;
            return this;
        }
        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        public Builder vehicleBrand(String vehicleBrand) {
            this.vehicleBrand = vehicleBrand;
            return this;
        }
        public Builder vehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
            return this;
        }
        public Builder vehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }
        public Builder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
            return this;
        }
        public Builder vehicleImageUrl(String vehicleImageUrl) {
            this.vehicleImageUrl = vehicleImageUrl;
            return this;
        }
        public Builder pickupDate(LocalDateTime pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }
        public Builder returnDate(LocalDateTime returnDate) {
            this.returnDate = returnDate;
            return this;
        }
        public Builder pickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
            return this;
        }
        public Builder returnLocation(String returnLocation) {
            this.returnLocation = returnLocation;
            return this;
        }
        public Builder baseAmount(BigDecimal baseAmount) {
            this.baseAmount = baseAmount;
            return this;
        }
        public Builder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }
        public Builder taxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
            return this;
        }
        public Builder securityDeposit(BigDecimal securityDeposit) {
            this.securityDeposit = securityDeposit;
            return this;
        }
        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }
        public Builder status(BookingStatus status) {
            this.status = status;
            return this;
        }
        public Builder couponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }
        public Builder cancellationReason(String cancellationReason) {
            this.cancellationReason = cancellationReason;
            return this;
        }
        public Builder isPaid(Boolean isPaid) {
            this.isPaid = isPaid;
            return this;
        }
        public Builder paymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BookingResponse build() {
            return new BookingResponse(this.id, this.bookingReference, this.userId, this.userName, this.userEmail, this.userPhone, this.vehicleId, this.vehicleBrand, this.vehicleModel, this.vehicleType, this.vehicleRegistrationNumber, this.vehicleImageUrl, this.pickupDate, this.returnDate, this.pickupLocation, this.returnLocation, this.baseAmount, this.discountAmount, this.taxAmount, this.securityDeposit, this.totalAmount, this.status, this.couponCode, this.cancellationReason, this.isPaid, this.paymentStatus, this.createdAt, this.updatedAt);
        }
    }
}
