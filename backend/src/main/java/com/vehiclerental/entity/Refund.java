package com.vehiclerental.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cancellationFee;

    @Column(columnDefinition = "TEXT")
    private String refundReason;

    @Column(length = 30)
    private String status = "PROCESSED";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Refund() {
    }

    public Refund(Long id, Booking booking, Payment payment, BigDecimal refundAmount, BigDecimal cancellationFee, String refundReason, String status, LocalDateTime createdAt) {
        this.id = id;
        this.booking = booking;
        this.payment = payment;
        this.refundAmount = refundAmount;
        this.cancellationFee = cancellationFee;
        this.refundReason = refundReason;
        this.status = status;
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

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getCancellationFee() {
        return this.cancellationFee;
    }

    public void setCancellationFee(BigDecimal cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public String getRefundReason() {
        return this.refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        private Payment payment;
        private BigDecimal refundAmount;
        private BigDecimal cancellationFee;
        private String refundReason;
        private String status;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder booking(Booking booking) {
            this.booking = booking;
            return this;
        }
        public Builder payment(Payment payment) {
            this.payment = payment;
            return this;
        }
        public Builder refundAmount(BigDecimal refundAmount) {
            this.refundAmount = refundAmount;
            return this;
        }
        public Builder cancellationFee(BigDecimal cancellationFee) {
            this.cancellationFee = cancellationFee;
            return this;
        }
        public Builder refundReason(String refundReason) {
            this.refundReason = refundReason;
            return this;
        }
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Refund build() {
            return new Refund(this.id, this.booking, this.payment, this.refundAmount, this.cancellationFee, this.refundReason, this.status, this.createdAt);
        }
    }
}
