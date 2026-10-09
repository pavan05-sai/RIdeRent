package com.vehiclerental.dto.response;

import java.util.List;

public class VehicleDetailsResponse {
    private VehicleResponse vehicle;
    private List<ReviewResponse> recentReviews;
    private Boolean isAvailableNow;
    private Long activeBookingsCount;

    public VehicleDetailsResponse() {
    }

    public VehicleDetailsResponse(VehicleResponse vehicle, List<ReviewResponse> recentReviews, Boolean isAvailableNow, Long activeBookingsCount) {
        this.vehicle = vehicle;
        this.recentReviews = recentReviews;
        this.isAvailableNow = isAvailableNow;
        this.activeBookingsCount = activeBookingsCount;
    }

    public VehicleResponse getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(VehicleResponse vehicle) {
        this.vehicle = vehicle;
    }

    public List<ReviewResponse> getRecentReviews() {
        return this.recentReviews;
    }

    public void setRecentReviews(List<ReviewResponse> recentReviews) {
        this.recentReviews = recentReviews;
    }

    public Boolean getIsAvailableNow() {
        return this.isAvailableNow;
    }

    public void setIsAvailableNow(Boolean isAvailableNow) {
        this.isAvailableNow = isAvailableNow;
    }
    public Boolean getAvailableNow() {
        return this.isAvailableNow;
    }
    public void setAvailableNow(Boolean isAvailableNow) {
        this.isAvailableNow = isAvailableNow;
    }

    public Long getActiveBookingsCount() {
        return this.activeBookingsCount;
    }

    public void setActiveBookingsCount(Long activeBookingsCount) {
        this.activeBookingsCount = activeBookingsCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private VehicleResponse vehicle;
        private List<ReviewResponse> recentReviews;
        private Boolean isAvailableNow;
        private Long activeBookingsCount;

        public Builder vehicle(VehicleResponse vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public Builder recentReviews(List<ReviewResponse> recentReviews) {
            this.recentReviews = recentReviews;
            return this;
        }
        public Builder isAvailableNow(Boolean isAvailableNow) {
            this.isAvailableNow = isAvailableNow;
            return this;
        }
        public Builder activeBookingsCount(Long activeBookingsCount) {
            this.activeBookingsCount = activeBookingsCount;
            return this;
        }

        public VehicleDetailsResponse build() {
            return new VehicleDetailsResponse(this.vehicle, this.recentReviews, this.isAvailableNow, this.activeBookingsCount);
        }
    }
}
