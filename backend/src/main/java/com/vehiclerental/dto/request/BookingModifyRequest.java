package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingModifyRequest {
    @NotNull(message = "Pickup date is required")
    private LocalDateTime pickupDate;

    @NotNull(message = "Return date is required")
    private LocalDateTime returnDate;

    private String pickupLocation;
    private String returnLocation;

    public BookingModifyRequest() {
    }

    public BookingModifyRequest(LocalDateTime pickupDate, LocalDateTime returnDate, String pickupLocation, String returnLocation) {
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime pickupDate;
        private LocalDateTime returnDate;
        private String pickupLocation;
        private String returnLocation;

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

        public BookingModifyRequest build() {
            return new BookingModifyRequest(this.pickupDate, this.returnDate, this.pickupLocation, this.returnLocation);
        }
    }
}
