package com.vehiclerental.dto.response;

import com.vehiclerental.entity.enums.PaymentMethod;
import com.vehiclerental.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private String bookingReference;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionReference;
    private String demoNote;
    private LocalDateTime createdAt;

    public PaymentResponse() {
    }

    public PaymentResponse(Long id, Long bookingId, String bookingReference, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, String transactionReference, String demoNote, LocalDateTime createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.bookingReference = bookingReference;
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

    public Long getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingReference() {
        return this.bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
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
        private Long bookingId;
        private String bookingReference;
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
        public Builder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }
        public Builder bookingReference(String bookingReference) {
            this.bookingReference = bookingReference;
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

        public PaymentResponse build() {
            return new PaymentResponse(this.id, this.bookingId, this.bookingReference, this.amount, this.paymentMethod, this.status, this.transactionReference, this.demoNote, this.createdAt);
        }
    }
}
