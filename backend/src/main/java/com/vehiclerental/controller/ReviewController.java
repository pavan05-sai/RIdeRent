package com.vehiclerental.controller;

import com.vehiclerental.dto.request.ReviewCreateRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.ReviewResponse;
import com.vehiclerental.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReviewCreateRequest request) {
        ReviewResponse review = reviewService.createReview(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Review submitted successfully", review));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getVehicleReviews(@PathVariable Long vehicleId) {
        List<ReviewResponse> reviews = reviewService.getReviewsForVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteUserReview(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
    }

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
}
