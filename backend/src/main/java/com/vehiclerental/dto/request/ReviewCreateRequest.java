package com.vehiclerental.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class ReviewCreateRequest {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @NotBlank(message = "Review comment is required")
    private String comment;

    public ReviewCreateRequest() {
    }

    public ReviewCreateRequest(Long vehicleId, Long bookingId, Integer rating, String comment) {
        this.vehicleId = vehicleId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long vehicleId;
        private Long bookingId;
        private Integer rating;
        private String comment;

        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        public Builder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }
        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ReviewCreateRequest build() {
            return new ReviewCreateRequest(this.vehicleId, this.bookingId, this.rating, this.comment);
        }
    }
}
