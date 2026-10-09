package com.vehiclerental.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String profilePhoto;
    private Boolean isActive;
    private List<String> roles;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(Long id, String fullName, String email, String phone, String profilePhoto, Boolean isActive, List<String> roles, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.isActive = isActive;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public Boolean getActive() {
        return this.isActive;
    }
    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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
        private String fullName;
        private String email;
        private String phone;
        private String profilePhoto;
        private Boolean isActive;
        private List<String> roles;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
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
        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this.id, this.fullName, this.email, this.phone, this.profilePhoto, this.isActive, this.roles, this.createdAt);
        }
    }
}
