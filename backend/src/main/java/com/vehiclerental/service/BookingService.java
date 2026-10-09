package com.vehiclerental.service;

import com.vehiclerental.dto.request.*;
import com.vehiclerental.dto.response.BookingQuoteResponse;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;

public interface BookingService {
    BookingQuoteResponse calculateQuote(BookingQuoteRequest request);
    BookingResponse createBooking(String userEmail, BookingCreateRequest request);
    Page<BookingResponse> getUserBookings(String userEmail, int page, int size);
    BookingResponse getCurrentActiveRental(String userEmail);
    BookingResponse getBookingById(Long id, String userEmail);
    BookingResponse modifyBooking(Long id, String userEmail, BookingModifyRequest request);
    BookingResponse extendBooking(Long id, String userEmail, BookingExtensionRequest request);
    BookingResponse cancelBooking(Long id, String userEmail, BookingCancelRequest request);
    Page<BookingResponse> getAllBookingsAdmin(String search, BookingStatus status, int page, int size);
    BookingResponse updateBookingStatusAdmin(Long id, BookingStatus status);
}
