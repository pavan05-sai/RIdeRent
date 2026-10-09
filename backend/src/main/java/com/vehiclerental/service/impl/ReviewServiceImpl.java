package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.ReviewCreateRequest;
import com.vehiclerental.dto.response.ReviewResponse;
import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.Review;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.entity.enums.BookingStatus;
import com.vehiclerental.entity.enums.RoleName;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ConflictException;
import com.vehiclerental.exception.ForbiddenException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.ReviewRepository;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.repository.VehicleRepository;
import com.vehiclerental.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(String userEmail, ReviewCreateRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVehicleId()));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));

        // Verified Renter Verification: Must be the user's booking, for this vehicle, and completed/confirmed
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You cannot review a booking that does not belong to you");
        }
        if (!booking.getVehicle().getId().equals(vehicle.getId())) {
            throw new BadRequestException("Booking does not match the specified vehicle");
        }
        if (booking.getStatus() != BookingStatus.COMPLETED && booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.ACTIVE) {
            throw new BadRequestException("Only verified renters with confirmed or completed rentals can submit reviews");
        }
        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new ConflictException("You have already reviewed this rental");
        }

        Review review = Review.builder()
                .vehicle(vehicle)
                .user(user)
                .booking(booking)
                .rating(request.getRating())
                .comment(request.getComment().trim())
                .isHidden(false)
                .build();

        Review savedReview = reviewRepository.save(review);

        // Recalculate vehicle rating
        updateVehicleAverageRating(vehicle.getId());

        return mapToReviewResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForVehicle(Long vehicleId) {
        return reviewRepository.findByVehicleIdAndIsHiddenFalseOrderByCreatedAtDesc(vehicleId).stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserReview(Long id, String userEmail) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !review.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Unauthorized to delete this review");
        }

        Long vehicleId = review.getVehicle().getId();
        reviewRepository.delete(review);
        updateVehicleAverageRating(vehicleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReviewsAdmin(int page, int size) {
        return reviewRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(this::mapToReviewResponse);
    }

    @Override
    @Transactional
    public ReviewResponse toggleReviewVisibilityAdmin(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        review.setIsHidden(!review.getIsHidden());
        Review saved = reviewRepository.save(review);
        updateVehicleAverageRating(saved.getVehicle().getId());
        return mapToReviewResponse(saved);
    }

    @Override
    @Transactional
    public void deleteReviewAdmin(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        Long vehicleId = review.getVehicle().getId();
        reviewRepository.delete(review);
        updateVehicleAverageRating(vehicleId);
    }

    private void updateVehicleAverageRating(Long vehicleId) {
        Double avgRating = reviewRepository.calculateAverageRatingForVehicle(vehicleId);
        Long totalReviews = reviewRepository.countReviewsForVehicle(vehicleId);

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle != null) {
            vehicle.setRating(avgRating != null ? BigDecimal.valueOf(avgRating).setScale(1, RoundingMode.HALF_UP) : BigDecimal.valueOf(5.0));
            vehicle.setTotalReviews(totalReviews != null ? totalReviews.intValue() : 0);
            vehicleRepository.save(vehicle);
        }
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .vehicleId(review.getVehicle().getId())
                .vehicleName(review.getVehicle().getBrand() + " " + review.getVehicle().getModel())
                .userId(review.getUser().getId())
                .userName(review.getUser().getFullName())
                .userPhoto(review.getUser().getProfilePhoto())
                .bookingId(review.getBooking().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .isHidden(review.getIsHidden())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public ReviewServiceImpl(ReviewRepository reviewRepository, BookingRepository bookingRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }
}
