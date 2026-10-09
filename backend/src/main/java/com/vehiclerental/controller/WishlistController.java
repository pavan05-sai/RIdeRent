package com.vehiclerental.controller;

import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        List<VehicleResponse> wishlist = wishlistService.getUserWishlist(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(wishlist));
    }

    @PostMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<Void>> addToWishlist(
            @PathVariable Long vehicleId,
            @AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.addToWishlist(userDetails.getUsername(), vehicleId);
        return ResponseEntity.ok(ApiResponse.success("Added to wishlist", null));
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @PathVariable Long vehicleId,
            @AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.removeFromWishlist(userDetails.getUsername(), vehicleId);
        return ResponseEntity.ok(ApiResponse.success("Removed from wishlist", null));
    }

    @GetMapping("/check/{vehicleId}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkWishlist(
            @PathVariable Long vehicleId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean inWishlist = wishlistService.isInWishlist(userDetails != null ? userDetails.getUsername() : null, vehicleId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("inWishlist", inWishlist)));
    }

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
}
