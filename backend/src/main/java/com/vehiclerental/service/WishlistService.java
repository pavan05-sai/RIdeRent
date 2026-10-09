package com.vehiclerental.service;

import com.vehiclerental.dto.response.VehicleResponse;

import java.util.List;

public interface WishlistService {
    List<VehicleResponse> getUserWishlist(String userEmail);
    void addToWishlist(String userEmail, Long vehicleId);
    void removeFromWishlist(String userEmail, Long vehicleId);
    boolean isInWishlist(String userEmail, Long vehicleId);
}
