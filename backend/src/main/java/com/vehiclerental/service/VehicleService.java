package com.vehiclerental.service;

import com.vehiclerental.dto.request.VehicleRequest;
import com.vehiclerental.dto.response.VehicleDetailsResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.entity.enums.VehicleType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VehicleService {
    Page<VehicleResponse> getVehicles(
            String search,
            VehicleType vehicleType,
            String fuelType,
            String transmission,
            String location,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer seats,
            VehicleStatus status,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    VehicleResponse getVehicleById(Long id);
    VehicleDetailsResponse getVehicleDetails(Long id);
    List<VehicleResponse> getFeaturedVehicles();
    boolean checkAvailability(Long vehicleId, LocalDateTime pickupDate, LocalDateTime returnDate);
    VehicleResponse createVehicle(VehicleRequest request);
    VehicleResponse updateVehicle(Long id, VehicleRequest request);
    void deleteVehicle(Long id);
    List<VehicleResponse> compareVehicles(List<Long> vehicleIds);
    void subscribeAvailabilityAlert(Long vehicleId, String userEmail);
}
