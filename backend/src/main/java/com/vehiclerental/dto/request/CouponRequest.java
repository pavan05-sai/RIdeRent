package com.vehiclerental.dto.request;

import com.vehiclerental.entity.enums.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponRequest {
    @NotBlank(message = "Coupon code is required")
    private String code;

    @NotNull(message = "Discount type is required")
    private DiscountType discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    private BigDecimal minimumBookingAmount;
    private BigDecimal maximumDiscount;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiryDate;

    private Integer usageLimit;
    private Boolean active;

    public CouponRequest() {
    }

    public CouponRequest(String code, DiscountType discountType, BigDecimal discountValue, BigDecimal minimumBookingAmount, BigDecimal maximumDiscount, LocalDateTime startDate, LocalDateTime expiryDate, Integer usageLimit, Boolean active) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumBookingAmount = minimumBookingAmount;
        this.maximumDiscount = maximumDiscount;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.usageLimit = usageLimit;
        this.active = active;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinimumBookingAmount() {
        return this.minimumBookingAmount;
    }

    public void setMinimumBookingAmount(BigDecimal minimumBookingAmount) {
        this.minimumBookingAmount = minimumBookingAmount;
    }

    public BigDecimal getMaximumDiscount() {
        return this.maximumDiscount;
    }

    public void setMaximumDiscount(BigDecimal maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getUsageLimit() {
        return this.usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    public Boolean isActive() {
        return this.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal minimumBookingAmount;
        private BigDecimal maximumDiscount;
        private LocalDateTime startDate;
        private LocalDateTime expiryDate;
        private Integer usageLimit;
        private Boolean active;

        public Builder code(String code) {
            this.code = code;
            return this;
        }
        public Builder discountType(DiscountType discountType) {
            this.discountType = discountType;
            return this;
        }
        public Builder discountValue(BigDecimal discountValue) {
            this.discountValue = discountValue;
            return this;
        }
        public Builder minimumBookingAmount(BigDecimal minimumBookingAmount) {
            this.minimumBookingAmount = minimumBookingAmount;
            return this;
        }
        public Builder maximumDiscount(BigDecimal maximumDiscount) {
            this.maximumDiscount = maximumDiscount;
            return this;
        }
        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }
        public Builder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }
        public Builder usageLimit(Integer usageLimit) {
            this.usageLimit = usageLimit;
            return this;
        }
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public CouponRequest build() {
            return new CouponRequest(this.code, this.discountType, this.discountValue, this.minimumBookingAmount, this.maximumDiscount, this.startDate, this.expiryDate, this.usageLimit, this.active);
        }
    }
}
