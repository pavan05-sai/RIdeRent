package com.vehiclerental.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "availability_alerts")
public class AvailabilityAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean notified = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public AvailabilityAlert() {
    }

    public AvailabilityAlert(Long id, Vehicle vehicle, User user, Boolean notified, LocalDateTime createdAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.user = user;
        this.notified = notified;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getNotified() {
        return this.notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
    public Boolean isNotified() {
        return this.notified;
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
        private Vehicle vehicle;
        private User user;
        private Boolean notified;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        public Builder notified(Boolean notified) {
            this.notified = notified;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AvailabilityAlert build() {
            return new AvailabilityAlert(this.id, this.vehicle, this.user, this.notified, this.createdAt);
        }
    }
}
