package com.vehiclerental.service;

import com.vehiclerental.dto.request.ReviewCreateRequest;
import com.vehiclerental.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(String userEmail, ReviewCreateRequest request);
    List<ReviewResponse> getReviewsForVehicle(Long vehicleId);
    void deleteUserReview(Long id, String userEmail);
    Page<ReviewResponse> getAllReviewsAdmin(int page, int size);
    ReviewResponse toggleReviewVisibilityAdmin(Long id);
    void deleteReviewAdmin(Long id);
}
