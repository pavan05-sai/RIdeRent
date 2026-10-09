package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.*;
import com.vehiclerental.dto.response.BookingQuoteResponse;
import com.vehiclerental.dto.response.BookingResponse;
import com.vehiclerental.dto.response.CouponResponse;
import com.vehiclerental.entity.*;
import com.vehiclerental.entity.enums.BookingStatus;
import com.vehiclerental.entity.enums.PaymentStatus;
import com.vehiclerental.entity.enums.RoleName;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ForbiddenException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.*;
import com.vehiclerental.service.BookingService;
import com.vehiclerental.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingExtensionRepository extensionRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% standard tax

    @Override
    @Transactional(readOnly = true)
    public BookingQuoteResponse calculateQuote(BookingQuoteRequest request) {
        validateDates(request.getPickupDate(), request.getReturnDate());

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId()));

        long days = calculateRentalDays(request.getPickupDate(), request.getReturnDate());
        BigDecimal baseAmount = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal discountAmount = BigDecimal.ZERO;
        String couponCode = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            CouponResponse couponRes = couponService.validateCoupon(request.getCouponCode().trim(), baseAmount);
            if (couponRes.getIsValid()) {
                discountAmount = couponRes.getDiscountValue();
                couponCode = couponRes.getCode();
            }
        }

        BigDecimal taxableAmount = baseAmount.subtract(discountAmount).max(BigDecimal.ZERO);
        BigDecimal taxAmount = taxableAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = taxableAmount.add(taxAmount).add(vehicle.getSecurityDeposit());

        return BookingQuoteResponse.builder()
                .vehicleId(vehicle.getId())
                .vehicleName(vehicle.getBrand() + " " + vehicle.getModel())
                .rentalDays(days)
                .pricePerDay(vehicle.getPricePerDay())
                .baseAmount(baseAmount)
                .discountAmount(discountAmount)
                .couponCode(couponCode)
                .taxAmount(taxAmount)
                .securityDeposit(vehicle.getSecurityDeposit())
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    @Transactional
    public BookingResponse createBooking(String userEmail, BookingCreateRequest request) {
        validateDates(request.getPickupDate(), request.getReturnDate());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId()));

        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE || vehicle.getStatus() == VehicleStatus.OUT_OF_SERVICE) {
            throw new BadRequestException("Vehicle is currently under maintenance or out of service and cannot be booked");
        }

        Long overlapping = bookingRepository.countOverlappingBookings(vehicle.getId(), request.getPickupDate(), request.getReturnDate(), null);
        if (overlapping > 0) {
            throw new BadRequestException("Vehicle is already booked for the selected dates. Please choose different dates.");
        }

        long days = calculateRentalDays(request.getPickupDate(), request.getReturnDate());
        BigDecimal baseAmount = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal discountAmount = BigDecimal.ZERO;
        Coupon appliedCoupon = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            CouponResponse couponRes = couponService.validateCoupon(request.getCouponCode().trim(), baseAmount);
            if (couponRes.getIsValid()) {
                discountAmount = couponRes.getDiscountValue();
                appliedCoupon = couponRepository.findByCodeIgnoreCase(request.getCouponCode().trim()).orElse(null);
            }
        }

        BigDecimal taxableAmount = baseAmount.subtract(discountAmount).max(BigDecimal.ZERO);
        BigDecimal taxAmount = taxableAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = taxableAmount.add(taxAmount).add(vehicle.getSecurityDeposit());

        String bookingRef = "RR-" + DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now()) + "-" +
                UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Booking booking = Booking.builder()
                .bookingReference(bookingRef)
                .user(user)
                .vehicle(vehicle)
                .pickupDate(request.getPickupDate())
                .returnDate(request.getReturnDate())
                .pickupLocation(request.getPickupLocation().trim())
                .returnLocation(request.getReturnLocation().trim())
                .baseAmount(baseAmount)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .securityDeposit(vehicle.getSecurityDeposit())
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING)
                .coupon(appliedCoupon)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // In-app notification
        notificationRepository.save(Notification.builder()
                .user(user)
                .title("Booking Created: " + bookingRef)
                .message("Your booking for " + vehicle.getBrand() + " " + vehicle.getModel() + " is pending demo payment.")
                .type("BOOKING")
                .isRead(false)
                .build());

        return mapToBookingResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> getUserBookings(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(this::mapToBookingResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getCurrentActiveRental(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        List<Booking> activeRentals = bookingRepository.findCurrentActiveRental(user.getId(), LocalDateTime.now());
        if (activeRentals.isEmpty()) {
            return null;
        }
        return mapToBookingResponse(activeRentals.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not authorized to view this booking");
        }

        return mapToBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse modifyBooking(Long id, String userEmail, BookingModifyRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not authorized to modify this booking");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Completed or cancelled bookings cannot be modified");
        }

        validateDates(request.getPickupDate(), request.getReturnDate());

        Long overlapping = bookingRepository.countOverlappingBookings(booking.getVehicle().getId(), request.getPickupDate(), request.getReturnDate(), booking.getId());
        if (overlapping > 0) {
            throw new BadRequestException("Vehicle is not available for the requested modification dates");
        }

        long days = calculateRentalDays(request.getPickupDate(), request.getReturnDate());
        BigDecimal baseAmount = booking.getVehicle().getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal taxableAmount = baseAmount.subtract(booking.getDiscountAmount()).max(BigDecimal.ZERO);
        BigDecimal taxAmount = taxableAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = taxableAmount.add(taxAmount).add(booking.getSecurityDeposit());

        booking.setPickupDate(request.getPickupDate());
        booking.setReturnDate(request.getReturnDate());
        if (request.getPickupLocation() != null && !request.getPickupLocation().isBlank()) {
            booking.setPickupLocation(request.getPickupLocation().trim());
        }
        if (request.getReturnLocation() != null && !request.getReturnLocation().isBlank()) {
            booking.setReturnLocation(request.getReturnLocation().trim());
        }
        booking.setBaseAmount(baseAmount);
        booking.setTaxAmount(taxAmount);
        booking.setTotalAmount(totalAmount);

        Booking updated = bookingRepository.save(booking);

        notificationRepository.save(Notification.builder()
                .user(booking.getUser())
                .title("Booking Modified: " + booking.getBookingReference())
                .message("Your booking schedule has been updated successfully.")
                .type("BOOKING")
                .isRead(false)
                .build());

        return mapToBookingResponse(updated);
    }

    @Override
    @Transactional
    public BookingResponse extendBooking(Long id, String userEmail, BookingExtensionRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not authorized to extend this booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.ACTIVE && booking.getStatus() != BookingStatus.EXTENDED) {
            throw new BadRequestException("Only confirmed or active bookings can be extended");
        }

        if (!request.getNewReturnDate().isAfter(booking.getReturnDate())) {
            throw new BadRequestException("Extended return date must be strictly after the current return date (" + booking.getReturnDate() + ")");
        }

        // Check availability between old return date and new return date
        Long overlapping = bookingRepository.countOverlappingBookings(booking.getVehicle().getId(), booking.getReturnDate(), request.getNewReturnDate(), booking.getId());
        if (overlapping > 0) {
            throw new BadRequestException("Cannot extend: Vehicle is reserved by another customer for the requested extension period");
        }

        long additionalDays = calculateRentalDays(booking.getReturnDate(), request.getNewReturnDate());
        BigDecimal additionalBase = booking.getVehicle().getPricePerDay().multiply(BigDecimal.valueOf(additionalDays));
        BigDecimal additionalTax = additionalBase.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal additionalTotal = additionalBase.add(additionalTax);

        BookingExtension extension = BookingExtension.builder()
                .booking(booking)
                .previousReturnDate(booking.getReturnDate())
                .extendedReturnDate(request.getNewReturnDate())
                .additionalDays((int) additionalDays)
                .additionalAmount(additionalTotal)
                .build();
        extensionRepository.save(extension);

        booking.setReturnDate(request.getNewReturnDate());
        booking.setBaseAmount(booking.getBaseAmount().add(additionalBase));
        booking.setTaxAmount(booking.getTaxAmount().add(additionalTax));
        booking.setTotalAmount(booking.getTotalAmount().add(additionalTotal));
        booking.setStatus(BookingStatus.EXTENDED);

        Booking saved = bookingRepository.save(booking);

        notificationRepository.save(Notification.builder()
                .user(booking.getUser())
                .title("Rental Extended: " + booking.getBookingReference())
                .message("Your rental period has been extended until " + request.getNewReturnDate() + ". Additional amount: ₹" + additionalTotal)
                .type("EXTENSION")
                .isRead(false)
                .build());

        return mapToBookingResponse(saved);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id, String userEmail, BookingCancelRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Booking is already " + booking.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        Duration durationUntilPickup = Duration.between(now, booking.getPickupDate());
        long hoursUntilPickup = durationUntilPickup.toHours();

        BigDecimal refundPercentage;
        BigDecimal feePercentage;

        if (hoursUntilPickup >= 48) {
            refundPercentage = BigDecimal.ONE; // 100%
            feePercentage = BigDecimal.ZERO;
        } else if (hoursUntilPickup >= 24) {
            refundPercentage = new BigDecimal("0.80"); // 80%
            feePercentage = new BigDecimal("0.20");
        } else {
            refundPercentage = new BigDecimal("0.50"); // 50%
            feePercentage = new BigDecimal("0.50");
        }

        BigDecimal refundableBase = booking.getBaseAmount().subtract(booking.getDiscountAmount()).max(BigDecimal.ZERO);
        BigDecimal refundAmount = refundableBase.multiply(refundPercentage).add(booking.getSecurityDeposit()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal cancellationFee = refundableBase.multiply(feePercentage).setScale(2, RoundingMode.HALF_UP);

        Payment payment = paymentRepository.findByBookingId(booking.getId()).orElse(null);

        Refund refund = Refund.builder()
                .booking(booking)
                .payment(payment)
                .refundAmount(refundAmount)
                .cancellationFee(cancellationFee)
                .refundReason(request.getReason())
                .status("PROCESSED")
                .build();
        refundRepository.save(refund);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(request.getReason());
        Booking saved = bookingRepository.save(booking);

        if (payment != null) {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        notificationRepository.save(Notification.builder()
                .user(booking.getUser())
                .title("Booking Cancelled: " + booking.getBookingReference())
                .message("Your booking has been cancelled. Refund amount of ₹" + refundAmount + " has been processed.")
                .type("CANCELLATION")
                .isRead(false)
                .build());

        return mapToBookingResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> getAllBookingsAdmin(String search, BookingStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        return bookingRepository.findBookingsWithFilters(searchParam, status, pageable)
                .map(this::mapToBookingResponse);
    }

    @Override
    @Transactional
    public BookingResponse updateBookingStatusAdmin(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        booking.setStatus(status);
        if (status == BookingStatus.ACTIVE) {
            booking.getVehicle().setStatus(VehicleStatus.RENTED);
            vehicleRepository.save(booking.getVehicle());
        } else if (status == BookingStatus.COMPLETED) {
            booking.getVehicle().setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(booking.getVehicle());
        }

        Booking updated = bookingRepository.save(booking);

        notificationRepository.save(Notification.builder()
                .user(booking.getUser())
                .title("Booking Status Updated: " + booking.getBookingReference())
                .message("Your booking status is now " + status)
                .type("BOOKING")
                .isRead(false)
                .build());

        return mapToBookingResponse(updated);
    }

    private void validateDates(LocalDateTime pickupDate, LocalDateTime returnDate) {
        if (pickupDate == null || returnDate == null) {
            throw new BadRequestException("Pickup and return dates are required");
        }
        if (returnDate.isBefore(pickupDate) || returnDate.isEqual(pickupDate)) {
            throw new BadRequestException("Return date and time must be strictly after pickup date and time");
        }
    }

    private long calculateRentalDays(LocalDateTime pickup, LocalDateTime ret) {
        long hours = Duration.between(pickup, ret).toHours();
        long days = (hours + 23) / 24; // Round up to full day
        return Math.max(1, days);
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        Payment payment = paymentRepository.findByBookingId(booking.getId()).orElse(null);
        boolean isPaid = payment != null && payment.getStatus() == PaymentStatus.SUCCESS;

        String vehicleImg = (booking.getVehicle().getImages() != null && !booking.getVehicle().getImages().isEmpty())
                ? booking.getVehicle().getImages().get(0).getImageUrl()
                : null;

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getFullName())
                .userEmail(booking.getUser().getEmail())
                .userPhone(booking.getUser().getPhone())
                .vehicleId(booking.getVehicle().getId())
                .vehicleBrand(booking.getVehicle().getBrand())
                .vehicleModel(booking.getVehicle().getModel())
                .vehicleType(booking.getVehicle().getVehicleType().name())
                .vehicleRegistrationNumber(booking.getVehicle().getRegistrationNumber())
                .vehicleImageUrl(vehicleImg)
                .pickupDate(booking.getPickupDate())
                .returnDate(booking.getReturnDate())
                .pickupLocation(booking.getPickupLocation())
                .returnLocation(booking.getReturnLocation())
                .baseAmount(booking.getBaseAmount())
                .discountAmount(booking.getDiscountAmount())
                .taxAmount(booking.getTaxAmount())
                .securityDeposit(booking.getSecurityDeposit())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .couponCode(booking.getCoupon() != null ? booking.getCoupon().getCode() : null)
                .cancellationReason(booking.getCancellationReason())
                .isPaid(isPaid)
                .paymentStatus(payment != null ? payment.getStatus().name() : "UNPAID")
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    public BookingServiceImpl(BookingRepository bookingRepository, BookingExtensionRepository extensionRepository, VehicleRepository vehicleRepository, UserRepository userRepository, CouponRepository couponRepository, CouponService couponService, RefundRepository refundRepository, PaymentRepository paymentRepository, NotificationRepository notificationRepository) {
        this.bookingRepository = bookingRepository;
        this.extensionRepository = extensionRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.couponService = couponService;
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
        this.notificationRepository = notificationRepository;
    }
}
