package com.vehiclerental.controller;

import com.vehiclerental.dto.request.DemoPaymentRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.PaymentResponse;
import com.vehiclerental.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/demo")
    public ResponseEntity<ApiResponse<PaymentResponse>> processDemoPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DemoPaymentRequest request) {
        PaymentResponse response = paymentService.processDemoPayment(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Demo payment processed successfully", response));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentForBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal UserDetails userDetails) {
        PaymentResponse response = paymentService.getPaymentForBooking(bookingId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
