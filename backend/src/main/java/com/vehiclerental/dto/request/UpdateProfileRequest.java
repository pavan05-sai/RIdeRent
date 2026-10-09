package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotBlank;
public class UpdateProfileRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    private String phone;
    private String profilePhoto;
    private String currentPassword;
    private String newPassword;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String fullName, String phone, String profilePhoto, String currentPassword, String newPassword) {
        this.fullName = fullName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String phone;
        private String profilePhoto;
        private String currentPassword;
        private String newPassword;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public Builder profilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
            return this;
        }
        public Builder currentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }
        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public UpdateProfileRequest build() {
            return new UpdateProfileRequest(this.fullName, this.phone, this.profilePhoto, this.currentPassword, this.newPassword);
        }
    }
}
