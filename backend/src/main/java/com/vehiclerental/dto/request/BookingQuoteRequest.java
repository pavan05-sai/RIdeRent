package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingQuoteRequest {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Pickup date is required")
    private LocalDateTime pickupDate;

    @NotNull(message = "Return date is required")
    private LocalDateTime returnDate;

    private String couponCode;

    public BookingQuoteRequest() {
    }

    public BookingQuoteRequest(Long vehicleId, LocalDateTime pickupDate, LocalDateTime returnDate, String couponCode) {
        this.vehicleId = vehicleId;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
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
        public Builder couponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }

        public BookingQuoteRequest build() {
            return new BookingQuoteRequest(this.vehicleId, this.pickupDate, this.returnDate, this.couponCode);
        }
    }
}
