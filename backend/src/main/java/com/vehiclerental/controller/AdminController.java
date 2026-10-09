package com.vehiclerental.controller;

import com.vehiclerental.dto.request.CouponRequest;
import com.vehiclerental.dto.request.MaintenanceCreateRequest;
import com.vehiclerental.dto.request.VehicleRequest;
import com.vehiclerental.dto.response.*;
import com.vehiclerental.entity.enums.BookingStatus;
import com.vehiclerental.entity.enums.MaintenanceStatus;
import com.vehiclerental.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminDashboardService adminDashboardService;
    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final MaintenanceService maintenanceService;
    private final ReviewService reviewService;

    // Dashboard
    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        DashboardStatsResponse stats = adminDashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/dashboard/analytics")
    public ResponseEntity<ApiResponse<DashboardAnalyticsResponse>> getDashboardAnalytics() {
        DashboardAnalyticsResponse analytics = adminDashboardService.getDashboardAnalytics();
        return ResponseEntity.ok(ApiResponse.success(analytics));
    }

    // User Management
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserResponse> users = adminDashboardService.getAllUsers(search, page, size);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<UserResponse>> toggleUserStatus(@PathVariable Long id) {
        UserResponse user = adminDashboardService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("User status toggled successfully", user));
    }

    // Vehicle Management
    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Vehicle created successfully", response));
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(ApiResponse.success("Vehicle updated successfully", response));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle deactivated successfully", null));
    }

    // Booking Management
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getAllBookings(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookingResponse> bookings = bookingService.getAllBookingsAdmin(search, status, page, size);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        BookingStatus status = BookingStatus.valueOf(body.get("status"));
        BookingResponse response = bookingService.updateBookingStatusAdmin(id, status);
        return ResponseEntity.ok(ApiResponse.success("Booking status updated", response));
    }

    // Payments
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PaymentResponse> payments = paymentService.getAllPaymentsAdmin(page, size);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }

    // Coupons
    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getAllCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(ApiResponse.success(coupons));
    }

    @PostMapping("/coupons")
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(@Valid @RequestBody CouponRequest request) {
        CouponResponse response = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Coupon created successfully", response));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.success("Coupon deactivated successfully", null));
    }

    // Maintenance
    @GetMapping("/maintenance")
    public ResponseEntity<ApiResponse<Page<MaintenanceResponse>>> getAllMaintenance(
            @RequestParam(required = false) MaintenanceStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MaintenanceResponse> records = maintenanceService.getAllMaintenance(status, page, size);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @PostMapping("/maintenance")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> createMaintenanceRecord(
            @Valid @RequestBody MaintenanceCreateRequest request) {
        MaintenanceResponse response = maintenanceService.createMaintenanceRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Maintenance recorded and vehicle status updated to MAINTENANCE", response));
    }

    @PutMapping("/maintenance/{id}/complete")
    public ResponseEntity<ApiResponse<MaintenanceResponse>> completeMaintenance(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String notes = body != null ? body.get("notes") : null;
        MaintenanceResponse response = maintenanceService.completeMaintenance(id, notes);
        return ResponseEntity.ok(ApiResponse.success("Maintenance completed, vehicle restored to AVAILABLE", response));
    }

    // Reviews Moderation
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponse> reviews = reviewService.getAllReviewsAdmin(page, size);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @PutMapping("/reviews/{id}/toggle-visibility")
    public ResponseEntity<ApiResponse<ReviewResponse>> toggleReviewVisibility(@PathVariable Long id) {
        ReviewResponse review = reviewService.toggleReviewVisibilityAdmin(id);
        return ResponseEntity.ok(ApiResponse.success("Review visibility updated", review));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReviewAdmin(@PathVariable Long id) {
        reviewService.deleteReviewAdmin(id);
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
    }

    public AdminController(AdminDashboardService adminDashboardService, VehicleService vehicleService, BookingService bookingService, PaymentService paymentService, CouponService couponService, MaintenanceService maintenanceService, ReviewService reviewService) {
        this.adminDashboardService = adminDashboardService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.couponService = couponService;
        this.maintenanceService = maintenanceService;
        this.reviewService = reviewService;
    }
}
