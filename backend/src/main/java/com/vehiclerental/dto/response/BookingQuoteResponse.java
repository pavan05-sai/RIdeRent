package com.vehiclerental.dto.response;

import java.math.BigDecimal;

public class BookingQuoteResponse {
    private Long vehicleId;
    private String vehicleName;
    private Long rentalDays;
    private BigDecimal pricePerDay;
    private BigDecimal baseAmount;
    private BigDecimal discountAmount;
    private String couponCode;
    private BigDecimal taxAmount; // e.g. 18% standard tax
    private BigDecimal securityDeposit;
    private BigDecimal totalAmount;

    public BookingQuoteResponse() {
    }

    public BookingQuoteResponse(Long vehicleId, String vehicleName, Long rentalDays, BigDecimal pricePerDay, BigDecimal baseAmount, BigDecimal discountAmount, String couponCode, BigDecimal taxAmount, BigDecimal securityDeposit, BigDecimal totalAmount) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.rentalDays = rentalDays;
        this.pricePerDay = pricePerDay;
        this.baseAmount = baseAmount;
        this.discountAmount = discountAmount;
        this.couponCode = couponCode;
        this.taxAmount = taxAmount;
        this.securityDeposit = securityDeposit;
        this.totalAmount = totalAmount;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return this.vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Long getRentalDays() {
        return this.rentalDays;
    }

    public void setRentalDays(Long rentalDays) {
        this.rentalDays = rentalDays;
    }

    public BigDecimal getPricePerDay() {
        return this.pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public BigDecimal getBaseAmount() {
        return this.baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCouponCode() {
        return this.couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getSecurityDeposit() {
        return this.securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long vehicleId;
        private String vehicleName;
        private Long rentalDays;
        private BigDecimal pricePerDay;
        private BigDecimal baseAmount;
        private BigDecimal discountAmount;
        private String couponCode;
        private BigDecimal taxAmount;
        private BigDecimal securityDeposit;
        private BigDecimal totalAmount;

        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        public Builder vehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
            return this;
        }
        public Builder rentalDays(Long rentalDays) {
            this.rentalDays = rentalDays;
            return this;
        }
        public Builder pricePerDay(BigDecimal pricePerDay) {
            this.pricePerDay = pricePerDay;
            return this;
        }
        public Builder baseAmount(BigDecimal baseAmount) {
            this.baseAmount = baseAmount;
            return this;
        }
        public Builder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }
        public Builder couponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }
        public Builder taxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
            return this;
        }
        public Builder securityDeposit(BigDecimal securityDeposit) {
            this.securityDeposit = securityDeposit;
            return this;
        }
        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BookingQuoteResponse build() {
            return new BookingQuoteResponse(this.vehicleId, this.vehicleName, this.rentalDays, this.pricePerDay, this.baseAmount, this.discountAmount, this.couponCode, this.taxAmount, this.securityDeposit, this.totalAmount);
        }
    }
}
