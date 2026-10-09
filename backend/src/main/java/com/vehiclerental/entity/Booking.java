package com.vehiclerental.entity;

import com.vehiclerental.entity.enums.BookingStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime pickupDate;

    @Column(nullable = false)
    private LocalDateTime returnDate;

    @Column(nullable = false, length = 120)
    private String pickupLocation;

    @Column(nullable = false, length = 120)
    private String returnLocation;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal securityDeposit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status = BookingStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(columnDefinition = "TEXT")
    private String cancellationReason;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Booking() {
    }

    public Booking(Long id, String bookingReference, User user, Vehicle vehicle, LocalDateTime pickupDate, LocalDateTime returnDate, String pickupLocation, String returnLocation, BigDecimal baseAmount, BigDecimal discountAmount, BigDecimal taxAmount, BigDecimal securityDeposit, BigDecimal totalAmount, BookingStatus status, Coupon coupon, String cancellationReason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.user = user;
        this.vehicle = vehicle;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.baseAmount = baseAmount;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.securityDeposit = securityDeposit;
        this.totalAmount = totalAmount;
        this.status = status;
        this.coupon = coupon;
        this.cancellationReason = cancellationReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return this.bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDateTime getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getPickupLocation() {
        return this.pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getReturnLocation() {
        return this.returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
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

    public BookingStatus getStatus() {
        return this.status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Coupon getCoupon() {
        return this.coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String bookingReference;
        private User user;
        private Vehicle vehicle;
        private LocalDateTime pickupDate;
        private LocalDateTime returnDate;
        private String pickupLocation;
        private String returnLocation;
        private BigDecimal baseAmount;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal securityDeposit;
        private BigDecimal totalAmount;
        private BookingStatus status;
        private Coupon coupon;
        private String cancellationReason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder bookingReference(String bookingReference) {
            this.bookingReference = bookingReference;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        public Builder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public Builder pickupDate(LocalDateTime pickupDate) {
            this.pickupDate = pickupDate;
            return this;
        }
        public Builder returnDate(LocalDateTime returnDate) {
            this.returnDate = returnDate;
            return this;
        }
        public Builder pickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
            return this;
        }
        public Builder returnLocation(String returnLocation) {
            this.returnLocation = returnLocation;
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
        public Builder status(BookingStatus status) {
            this.status = status;
            return this;
        }
        public Builder coupon(Coupon coupon) {
            this.coupon = coupon;
            return this;
        }
        public Builder cancellationReason(String cancellationReason) {
            this.cancellationReason = cancellationReason;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Booking build() {
            return new Booking(this.id, this.bookingReference, this.user, this.vehicle, this.pickupDate, this.returnDate, this.pickupLocation, this.returnLocation, this.baseAmount, this.discountAmount, this.taxAmount, this.securityDeposit, this.totalAmount, this.status, this.coupon, this.cancellationReason, this.createdAt, this.updatedAt);
        }
    }
}
