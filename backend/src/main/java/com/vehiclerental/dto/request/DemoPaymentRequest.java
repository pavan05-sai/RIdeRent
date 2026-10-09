package com.vehiclerental.dto.request;

import com.vehiclerental.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
public class DemoPaymentRequest {
    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String simulatedCardNumber;
    private String simulatedUpiId;

    public DemoPaymentRequest() {
    }

    public DemoPaymentRequest(Long bookingId, PaymentMethod paymentMethod, String simulatedCardNumber, String simulatedUpiId) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.simulatedCardNumber = simulatedCardNumber;
        this.simulatedUpiId = simulatedUpiId;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSimulatedCardNumber() {
        return this.simulatedCardNumber;
    }

    public void setSimulatedCardNumber(String simulatedCardNumber) {
        this.simulatedCardNumber = simulatedCardNumber;
    }

    public String getSimulatedUpiId() {
        return this.simulatedUpiId;
    }

    public void setSimulatedUpiId(String simulatedUpiId) {
        this.simulatedUpiId = simulatedUpiId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long bookingId;
        private PaymentMethod paymentMethod;
        private String simulatedCardNumber;
        private String simulatedUpiId;

        public Builder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }
        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }
        public Builder simulatedCardNumber(String simulatedCardNumber) {
            this.simulatedCardNumber = simulatedCardNumber;
            return this;
        }
        public Builder simulatedUpiId(String simulatedUpiId) {
            this.simulatedUpiId = simulatedUpiId;
            return this;
        }

        public DemoPaymentRequest build() {
            return new DemoPaymentRequest(this.bookingId, this.paymentMethod, this.simulatedCardNumber, this.simulatedUpiId);
        }
    }
}
