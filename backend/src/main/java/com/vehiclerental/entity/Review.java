package com.vehiclerental.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Integer rating; // 1 to 5

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(Long id, Vehicle vehicle, User user, Booking booking, Integer rating, String comment, Boolean isHidden, LocalDateTime createdAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.user = user;
        this.booking = booking;
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

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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
        private Vehicle vehicle;
        private User user;
        private Booking booking;
        private Integer rating;
        private String comment;
        private Boolean isHidden;
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
        public Builder booking(Booking booking) {
            this.booking = booking;
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

        public Review build() {
            return new Review(this.id, this.vehicle, this.user, this.booking, this.rating, this.comment, this.isHidden, this.createdAt);
        }
    }
}
