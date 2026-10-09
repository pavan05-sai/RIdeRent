package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.CouponRequest;
import com.vehiclerental.dto.response.CouponResponse;
import com.vehiclerental.entity.Coupon;
import com.vehiclerental.entity.enums.DiscountType;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ConflictException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.CouponRepository;
import com.vehiclerental.service.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    @Transactional(readOnly = true)
    public CouponResponse validateCoupon(String code, BigDecimal bookingAmount) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Coupon code cannot be empty");
        }

        Coupon coupon = couponRepository.findByCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon code '" + code + "' is not valid"));

        LocalDateTime now = LocalDateTime.now();

        if (!coupon.getActive()) {
            return buildInvalidResponse(coupon, "This coupon is no longer active");
        }
        if (now.isBefore(coupon.getStartDate())) {
            return buildInvalidResponse(coupon, "This coupon is not valid yet");
        }
        if (now.isAfter(coupon.getExpiryDate())) {
            return buildInvalidResponse(coupon, "This coupon has expired");
        }
        if (coupon.getTimesUsed() >= coupon.getUsageLimit()) {
            return buildInvalidResponse(coupon, "This coupon has reached its maximum usage limit");
        }
        if (coupon.getMinimumBookingAmount() != null && bookingAmount.compareTo(coupon.getMinimumBookingAmount()) < 0) {
            return buildInvalidResponse(coupon, "Minimum booking amount of ₹" + coupon.getMinimumBookingAmount() + " required");
        }

        BigDecimal discount = calculateDiscount(coupon, bookingAmount);

        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(discount)
                .minimumBookingAmount(coupon.getMinimumBookingAmount())
                .maximumDiscount(coupon.getMaximumDiscount())
                .startDate(coupon.getStartDate())
                .expiryDate(coupon.getExpiryDate())
                .usageLimit(coupon.getUsageLimit())
                .timesUsed(coupon.getTimesUsed())
                .active(coupon.getActive())
                .isValid(true)
                .message("Coupon applied successfully!")
                .build();
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal amount) {
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            BigDecimal calculated = amount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (coupon.getMaximumDiscount() != null && calculated.compareTo(coupon.getMaximumDiscount()) > 0) {
                return coupon.getMaximumDiscount();
            }
            return calculated;
        } else {
            return coupon.getDiscountValue().min(amount);
        }
    }

    private CouponResponse buildInvalidResponse(Coupon coupon, String message) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(BigDecimal.ZERO)
                .isValid(false)
                .message(message)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(c -> CouponResponse.builder()
                        .id(c.getId())
                        .code(c.getCode())
                        .discountType(c.getDiscountType())
                        .discountValue(c.getDiscountValue())
                        .minimumBookingAmount(c.getMinimumBookingAmount())
                        .maximumDiscount(c.getMaximumDiscount())
                        .startDate(c.getStartDate())
                        .expiryDate(c.getExpiryDate())
                        .usageLimit(c.getUsageLimit())
                        .timesUsed(c.getTimesUsed())
                        .active(c.getActive())
                        .isValid(c.getActive() && LocalDateTime.now().isBefore(c.getExpiryDate()))
                        .message("Coupon active")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCodeIgnoreCase(request.getCode().trim())) {
            throw new ConflictException("Coupon code already exists: " + request.getCode());
        }

        if (request.getStartDate().isAfter(request.getExpiryDate())) {
            throw new BadRequestException("Coupon expiry date must be after start date");
        }

        Coupon coupon = Coupon.builder()
                .code(request.getCode().trim().toUpperCase())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minimumBookingAmount(request.getMinimumBookingAmount() != null ? request.getMinimumBookingAmount() : BigDecimal.ZERO)
                .maximumDiscount(request.getMaximumDiscount())
                .startDate(request.getStartDate())
                .expiryDate(request.getExpiryDate())
                .usageLimit(request.getUsageLimit() != null ? request.getUsageLimit() : 1000)
                .timesUsed(0)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Coupon saved = couponRepository.save(coupon);

        return CouponResponse.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .discountType(saved.getDiscountType())
                .discountValue(saved.getDiscountValue())
                .minimumBookingAmount(saved.getMinimumBookingAmount())
                .maximumDiscount(saved.getMaximumDiscount())
                .startDate(saved.getStartDate())
                .expiryDate(saved.getExpiryDate())
                .usageLimit(saved.getUsageLimit())
                .timesUsed(saved.getTimesUsed())
                .active(saved.getActive())
                .isValid(true)
                .message("Created successfully")
                .build();
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with ID: " + id));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
}
