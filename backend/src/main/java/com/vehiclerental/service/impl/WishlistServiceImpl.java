package com.vehiclerental.service.impl;

import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.entity.Wishlist;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.repository.VehicleRepository;
import com.vehiclerental.repository.WishlistRepository;
import com.vehiclerental.service.VehicleService;
import com.vehiclerental.service.WishlistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleServiceImpl vehicleService;

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getUserWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(w -> vehicleService.mapToVehicleResponse(w.getVehicle()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addToWishlist(String userEmail, Long vehicleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));

        if (!wishlistRepository.existsByUserIdAndVehicleId(user.getId(), vehicleId)) {
            wishlistRepository.save(Wishlist.builder()
                    .user(user)
                    .vehicle(vehicle)
                    .build());
        }
    }

    @Override
    @Transactional
    public void removeFromWishlist(String userEmail, Long vehicleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        wishlistRepository.findByUserIdAndVehicleId(user.getId(), vehicleId)
                .ifPresent(wishlistRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInWishlist(String userEmail, Long vehicleId) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return false;
        return wishlistRepository.existsByUserIdAndVehicleId(user.getId(), vehicleId);
    }

    public WishlistServiceImpl(WishlistRepository wishlistRepository, UserRepository userRepository, VehicleRepository vehicleRepository, VehicleServiceImpl vehicleService) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }
}
