package com.vehiclerental.controller;

import com.vehiclerental.dto.request.CompareVehiclesRequest;
import com.vehiclerental.dto.response.ApiResponse;
import com.vehiclerental.dto.response.VehicleDetailsResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.entity.enums.VehicleType;
import com.vehiclerental.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VehicleResponse>>> getVehicles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) VehicleType vehicleType,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer seats,
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<VehicleResponse> vehicles = vehicleService.getVehicles(
                search, vehicleType, fuelType, transmission, location,
                minPrice, maxPrice, seats, status, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.success(vehicle));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<VehicleDetailsResponse>> getVehicleDetails(@PathVariable Long id) {
        VehicleDetailsResponse details = vehicleService.getVehicleDetails(id);
        return ResponseEntity.ok(ApiResponse.success(details));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getFeaturedVehicles() {
        List<VehicleResponse> featured = vehicleService.getFeaturedVehicles();
        return ResponseEntity.ok(ApiResponse.success(featured));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnDate) {

        boolean isAvailable = vehicleService.checkAvailability(id, pickupDate, returnDate);
        return ResponseEntity.ok(ApiResponse.success(Map.of("available", isAvailable)));
    }

    @PostMapping("/compare")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> compareVehicles(@Valid @RequestBody CompareVehiclesRequest request) {
        List<VehicleResponse> comparison = vehicleService.compareVehicles(request.getVehicleIds());
        return ResponseEntity.ok(ApiResponse.success(comparison));
    }

    @PostMapping("/{id}/notify-availability")
    public ResponseEntity<ApiResponse<Void>> subscribeAvailability(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        vehicleService.subscribeAvailabilityAlert(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("You will be notified as soon as this vehicle is available", null));
    }

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
}
