package com.vehiclerental.controller;

import com.vehiclerental.dto.request.CouponValidateRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.CouponResponse;
import com.vehiclerental.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CouponResponse>> validateCoupon(@Valid @RequestBody CouponValidateRequest request) {
        CouponResponse response = couponService.validateCoupon(request.getCode(), request.getBookingAmount());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }
}
