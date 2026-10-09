package com.vehiclerental.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CouponValidateRequest {
    @NotBlank(message = "Coupon code is required")
    private String code;

    @NotNull(message = "Booking base amount is required")
    @DecimalMin(value = "0.0", message = "Amount must be positive")
    private BigDecimal bookingAmount;

    public CouponValidateRequest() {
    }

    public CouponValidateRequest(String code, BigDecimal bookingAmount) {
        this.code = code;
        this.bookingAmount = bookingAmount;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getBookingAmount() {
        return this.bookingAmount;
    }

    public void setBookingAmount(BigDecimal bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private BigDecimal bookingAmount;

        public Builder code(String code) {
            this.code = code;
            return this;
        }
        public Builder bookingAmount(BigDecimal bookingAmount) {
            this.bookingAmount = bookingAmount;
            return this;
        }

        public CouponValidateRequest build() {
            return new CouponValidateRequest(this.code, this.bookingAmount);
        }
    }
}
