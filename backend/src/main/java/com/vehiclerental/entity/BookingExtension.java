package com.vehiclerental.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_extensions")
public class BookingExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private LocalDateTime previousReturnDate;

    @Column(nullable = false)
    private LocalDateTime extendedReturnDate;

    @Column(nullable = false)
    private Integer additionalDays;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal additionalAmount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public BookingExtension() {
    }

    public BookingExtension(Long id, Booking booking, LocalDateTime previousReturnDate, LocalDateTime extendedReturnDate, Integer additionalDays, BigDecimal additionalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.booking = booking;
        this.previousReturnDate = previousReturnDate;
        this.extendedReturnDate = extendedReturnDate;
        this.additionalDays = additionalDays;
        this.additionalAmount = additionalAmount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LocalDateTime getPreviousReturnDate() {
        return this.previousReturnDate;
    }

    public void setPreviousReturnDate(LocalDateTime previousReturnDate) {
        this.previousReturnDate = previousReturnDate;
    }

    public LocalDateTime getExtendedReturnDate() {
        return this.extendedReturnDate;
    }

    public void setExtendedReturnDate(LocalDateTime extendedReturnDate) {
        this.extendedReturnDate = extendedReturnDate;
    }

    public Integer getAdditionalDays() {
        return this.additionalDays;
    }

    public void setAdditionalDays(Integer additionalDays) {
        this.additionalDays = additionalDays;
    }

    public BigDecimal getAdditionalAmount() {
        return this.additionalAmount;
    }

    public void setAdditionalAmount(BigDecimal additionalAmount) {
        this.additionalAmount = additionalAmount;
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
        private Booking booking;
        private LocalDateTime previousReturnDate;
        private LocalDateTime extendedReturnDate;
        private Integer additionalDays;
        private BigDecimal additionalAmount;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder booking(Booking booking) {
            this.booking = booking;
            return this;
        }
        public Builder previousReturnDate(LocalDateTime previousReturnDate) {
            this.previousReturnDate = previousReturnDate;
            return this;
        }
        public Builder extendedReturnDate(LocalDateTime extendedReturnDate) {
            this.extendedReturnDate = extendedReturnDate;
            return this;
        }
        public Builder additionalDays(Integer additionalDays) {
            this.additionalDays = additionalDays;
            return this;
        }
        public Builder additionalAmount(BigDecimal additionalAmount) {
            this.additionalAmount = additionalAmount;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BookingExtension build() {
            return new BookingExtension(this.id, this.booking, this.previousReturnDate, this.extendedReturnDate, this.additionalDays, this.additionalAmount, this.createdAt);
        }
    }
}
