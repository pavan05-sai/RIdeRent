package com.vehiclerental.dto.response;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private String type;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String title, String message, String type, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    public Boolean getRead() {
        return this.isRead;
    }
    public void setRead(Boolean isRead) {
        this.isRead = isRead;
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
        private String title;
        private String message;
        private String type;
        private Boolean isRead;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        public Builder type(String type) {
            this.type = type;
            return this;
        }
        public Builder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationResponse build() {
            return new NotificationResponse(this.id, this.title, this.message, this.type, this.isRead, this.createdAt);
        }
    }
}
