package com.vehiclerental.entity;

import com.vehiclerental.entity.enums.DiscountType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType discountType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(precision = 10, scale = 2)
    private BigDecimal minimumBookingAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal maximumDiscount;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private Integer usageLimit = 1000;

    @Column(nullable = false)
    private Integer timesUsed = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Coupon() {
    }

    public Coupon(Long id, String code, DiscountType discountType, BigDecimal discountValue, BigDecimal minimumBookingAmount, BigDecimal maximumDiscount, LocalDateTime startDate, LocalDateTime expiryDate, Integer usageLimit, Integer timesUsed, Boolean active, LocalDateTime createdAt) {
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
        this.createdAt = createdAt;
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
        private LocalDateTime createdAt;

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
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Coupon build() {
            return new Coupon(this.id, this.code, this.discountType, this.discountValue, this.minimumBookingAmount, this.maximumDiscount, this.startDate, this.expiryDate, this.usageLimit, this.timesUsed, this.active, this.createdAt);
        }
    }
}
