package com.vehiclerental.dto.response;

import com.vehiclerental.entity.enums.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponResponse {
    private Long id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minimumBookingAmount;
    private BigDecimal maximumDiscount;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private Integer usageLimit;
    private Integer timesUsed;
    private Boolean active;
    private Boolean isValid;
    private String message;

    public CouponResponse() {
    }

    public CouponResponse(Long id, String code, DiscountType discountType, BigDecimal discountValue, BigDecimal minimumBookingAmount, BigDecimal maximumDiscount, LocalDateTime startDate, LocalDateTime expiryDate, Integer usageLimit, Integer timesUsed, Boolean active, Boolean isValid, String message) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumBookingAmount = minimumBookingAmount;
        this.maximumDiscount = maximumDiscount;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.usageLimit = usageLimit;
        this.timesUsed = timesUsed;
        this.active = active;
        this.isValid = isValid;
        this.message = message;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getTimesUsed() {
        return this.timesUsed;
    }

    public void setTimesUsed(Integer timesUsed) {
        this.timesUsed = timesUsed;
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

    public Boolean getIsValid() {
        return this.isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
    public Boolean getValid() {
        return this.isValid;
    }
    public void setValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String code;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal minimumBookingAmount;
        private BigDecimal maximumDiscount;
        private LocalDateTime startDate;
        private LocalDateTime expiryDate;
        private Integer usageLimit;
        private Integer timesUsed;
        private Boolean active;
        private Boolean isValid;
        private String message;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
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
        public Builder timesUsed(Integer timesUsed) {
            this.timesUsed = timesUsed;
            return this;
        }
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }
        public Builder isValid(Boolean isValid) {
            this.isValid = isValid;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public CouponResponse build() {
            return new CouponResponse(this.id, this.code, this.discountType, this.discountValue, this.minimumBookingAmount, this.maximumDiscount, this.startDate, this.expiryDate, this.usageLimit, this.timesUsed, this.active, this.isValid, this.message);
        }
    }
}
