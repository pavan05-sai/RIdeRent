package com.vehiclerental.service;

import com.vehiclerental.dto.request.DemoPaymentRequest;
import com.vehiclerental.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;

public interface PaymentService {
    PaymentResponse processDemoPayment(String userEmail, DemoPaymentRequest request);
    PaymentResponse getPaymentForBooking(Long bookingId, String userEmail);
    Page<PaymentResponse> getAllPaymentsAdmin(int page, int size);
}
