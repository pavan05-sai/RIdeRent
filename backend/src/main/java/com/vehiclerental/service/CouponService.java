package com.vehiclerental.service;

import com.vehiclerental.dto.request.CouponRequest;
import com.vehiclerental.dto.response.CouponResponse;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    CouponResponse validateCoupon(String code, BigDecimal bookingAmount);
    List<CouponResponse> getAllCoupons();
    CouponResponse createCoupon(CouponRequest request);
    void deleteCoupon(Long id);
}
