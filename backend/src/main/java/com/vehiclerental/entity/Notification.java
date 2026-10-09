package com.vehiclerental.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(length = 50)
    private String type; // BOOKING, PAYMENT, CANCELLATION, EXTENSION, AVAILABILITY, REMINDER

    @Column(nullable = false)
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(Long id, User user, String title, String message, String type, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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
        private User user;
        private String title;
        private String message;
        private String type;
        private Boolean isRead;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
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

        public Notification build() {
            return new Notification(this.id, this.user, this.title, this.message, this.type, this.isRead, this.createdAt);
        }
    }
}
