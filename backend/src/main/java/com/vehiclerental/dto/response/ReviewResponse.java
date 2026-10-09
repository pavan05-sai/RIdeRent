package com.vehiclerental.dto.response;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long vehicleId;
    private String vehicleName;
    private Long userId;
    private String userName;
    private String userPhoto;
    private Long bookingId;
    private Integer rating;
    private String comment;
    private Boolean isHidden;
    private LocalDateTime createdAt;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, Long vehicleId, String vehicleName, Long userId, String userName, String userPhoto, Long bookingId, Integer rating, String comment, Boolean isHidden, LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.userId = userId;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
        this.isHidden = isHidden;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return this.vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
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

    public String getUserPhoto() {
        return this.userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
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

    public Boolean getIsHidden() {
        return this.isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }
    public Boolean getHidden() {
        return this.isHidden;
    }
    public void setHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long vehicleId;
        private String vehicleName;
        private Long userId;
        private String userName;
        private String userPhoto;
        private Long bookingId;
        private Integer rating;
        private String comment;
        private Boolean isHidden;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        public Builder vehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
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
        public Builder userPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
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
        public Builder isHidden(Boolean isHidden) {
            this.isHidden = isHidden;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReviewResponse build() {
            return new ReviewResponse(this.id, this.vehicleId, this.vehicleName, this.userId, this.userName, this.userPhoto, this.bookingId, this.rating, this.comment, this.isHidden, this.createdAt);
        }
    }
}
