package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.DemoPaymentRequest;
import com.vehiclerental.dto.response.PaymentResponse;
import com.vehiclerental.entity.*;
import com.vehiclerental.entity.enums.BookingStatus;
import com.vehiclerental.entity.enums.PaymentStatus;
import com.vehiclerental.entity.enums.RoleName;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ForbiddenException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.*;
import com.vehiclerental.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public PaymentResponse processDemoPayment(String userEmail, DemoPaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + request.getBookingId()));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You cannot pay for another user's booking");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Booking is already in status: " + booking.getStatus());
        }

        String txnRef = "TXN-DEMO-" + DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now()) + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.SUCCESS)
                .transactionReference(txnRef)
                .demoNote("Simulated Demo Payment - RideRent Platform")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // Update vehicle status
        Vehicle vehicle = booking.getVehicle();
        if (vehicle.getStatus() == VehicleStatus.AVAILABLE) {
            vehicle.setStatus(VehicleStatus.BOOKED);
            vehicleRepository.save(vehicle);
        }

        // Increment coupon usage if used
        if (booking.getCoupon() != null) {
            Coupon coupon = booking.getCoupon();
            coupon.setTimesUsed(coupon.getTimesUsed() + 1);
            couponRepository.save(coupon);
        }

        // In-app notification
        notificationRepository.save(Notification.builder()
                .user(user)
                .title("Payment Successful: ₹" + booking.getTotalAmount())
                .message("Demo payment for booking " + booking.getBookingReference() + " was confirmed via " + request.getPaymentMethod() + ". Transaction ID: " + txnRef)
                .type("PAYMENT")
                .isRead(false)
                .build());

        return mapToPaymentResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentForBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Unauthorized to access payment records");
        }

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment record found for booking " + bookingId));

        return mapToPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPaymentsAdmin(int page, int size) {
        return paymentRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(this::mapToPaymentResponse);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .bookingReference(payment.getBooking().getBookingReference())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .demoNote(payment.getDemoNote())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository, VehicleRepository vehicleRepository, UserRepository userRepository, CouponRepository couponRepository, NotificationRepository notificationRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.notificationRepository = notificationRepository;
    }
}
