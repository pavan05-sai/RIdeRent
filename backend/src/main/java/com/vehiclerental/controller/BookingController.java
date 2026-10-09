package com.vehiclerental.controller;

import com.vehiclerental.dto.request.*;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.BookingQuoteResponse;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.service.BookingService;
import com.vehiclerental.service.PdfInvoiceService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PdfInvoiceService pdfInvoiceService;

    @PostMapping("/quote")
    public ResponseEntity<ApiResponse<BookingQuoteResponse>> calculateQuote(@Valid @RequestBody BookingQuoteRequest request) {
        BookingQuoteResponse quote = bookingService.calculateQuote(request);
        return ResponseEntity.ok(ApiResponse.success("Price calculated successfully", quote));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingCreateRequest request) {
        BookingResponse booking = bookingService.createBooking(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Booking created successfully", booking));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BookingResponse> bookings = bookingService.getUserBookings(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/my/active")
    public ResponseEntity<ApiResponse<BookingResponse>> getActiveRental(@AuthenticationPrincipal UserDetails userDetails) {
        BookingResponse activeRental = bookingService.getCurrentActiveRental(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(activeRental));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        BookingResponse booking = bookingService.getBookingById(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<ApiResponse<BookingResponse>> modifyBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingModifyRequest request) {
        BookingResponse booking = bookingService.modifyBooking(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Booking modified successfully", booking));
    }

    @PostMapping("/{id}/extend")
    public ResponseEntity<ApiResponse<BookingResponse>> extendBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingExtensionRequest request) {
        BookingResponse booking = bookingService.extendBooking(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Rental extended successfully", booking));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingCancelRequest request) {
        BookingResponse booking = bookingService.cancelBooking(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
    }

    @GetMapping("/{id}/invoice/pdf")
    public ResponseEntity<InputStreamResource> downloadInvoicePdf(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ByteArrayInputStream bis = pdfInvoiceService.generateInvoicePdf(id, userDetails.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=RideRent-Invoice-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/{id}/agreement/pdf")
    public ResponseEntity<InputStreamResource> downloadRentalAgreementPdf(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ByteArrayInputStream bis = pdfInvoiceService.generateRentalAgreementPdf(id, userDetails.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=RideRent-Agreement-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    public BookingController(BookingService bookingService, PdfInvoiceService pdfInvoiceService) {
        this.bookingService = bookingService;
        this.pdfInvoiceService = pdfInvoiceService;
    }
}
