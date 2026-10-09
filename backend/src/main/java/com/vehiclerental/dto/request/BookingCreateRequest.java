package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingCreateRequest {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Pickup date and time is required")
    private LocalDateTime pickupDate;

    @NotNull(message = "Return date and time is required")
    private LocalDateTime returnDate;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotBlank(message = "Return location is required")
    private String returnLocation;

    private String couponCode;

    public BookingCreateRequest() {
    }

    public BookingCreateRequest(Long vehicleId, LocalDateTime pickupDate, LocalDateTime returnDate, String pickupLocation, String returnLocation, String couponCode) {
        this.vehicleId = vehicleId;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.couponCode = couponCode;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getCouponCode() {
        return this.couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long vehicleId;
        private LocalDateTime pickupDate;
        private LocalDateTime returnDate;
        private String pickupLocation;
        private String returnLocation;
        private String couponCode;

        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
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
        public Builder couponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }

        public BookingCreateRequest build() {
            return new BookingCreateRequest(this.vehicleId, this.pickupDate, this.returnDate, this.pickupLocation, this.returnLocation, this.couponCode);
        }
    }
}
