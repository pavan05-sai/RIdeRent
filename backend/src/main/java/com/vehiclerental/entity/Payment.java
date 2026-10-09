package com.vehiclerental.entity;

import com.vehiclerental.entity.enums.PaymentMethod;
import com.vehiclerental.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false, unique = true, length = 64)
    private String transactionReference;

    @Column(length = 100)
    private String demoNote = "Simulated Demo Payment - RideRent Platform";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Payment() {
    }

    public Payment(Long id, Booking booking, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, String transactionReference, String demoNote, LocalDateTime createdAt) {
        this.id = id;
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionReference = transactionReference;
        this.demoNote = demoNote;
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

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionReference() {
        return this.transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getDemoNote() {
        return this.demoNote;
    }

    public void setDemoNote(String demoNote) {
        this.demoNote = demoNote;
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
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private PaymentStatus status;
        private String transactionReference;
        private String demoNote;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder booking(Booking booking) {
            this.booking = booking;
            return this;
        }
        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }
        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }
        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }
        public Builder transactionReference(String transactionReference) {
            this.transactionReference = transactionReference;
            return this;
        }
        public Builder demoNote(String demoNote) {
            this.demoNote = demoNote;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Payment build() {
            return new Payment(this.id, this.booking, this.amount, this.paymentMethod, this.status, this.transactionReference, this.demoNote, this.createdAt);
        }
    }
}
