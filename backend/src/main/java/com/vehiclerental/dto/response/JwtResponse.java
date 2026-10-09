package com.vehiclerental.dto.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String profilePhoto;
    private List<String> roles;

    public JwtResponse() {
    }

    public JwtResponse(String token, String type, Long id, String email, String fullName, String phone, String profilePhoto, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.roles = roles;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String type;
        private Long id;
        private String email;
        private String fullName;
        private String phone;
        private String profilePhoto;
        private List<String> roles;

        public Builder token(String token) {
            this.token = token;
            return this;
        }
        public Builder type(String type) {
            this.type = type;
            return this;
        }
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
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
        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public JwtResponse build() {
            return new JwtResponse(this.token, this.type, this.id, this.email, this.fullName, this.phone, this.profilePhoto, this.roles);
        }
    }
}
