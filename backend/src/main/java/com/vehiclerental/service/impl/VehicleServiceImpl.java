package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.VehicleRequest;
import com.vehiclerental.dto.response.ReviewResponse;
import com.vehiclerental.dto.response.VehicleDetailsResponse;
import com.vehiclerental.dto.response.VehicleResponse;
import com.vehiclerental.entity.AvailabilityAlert;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.entity.VehicleImage;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.entity.enums.VehicleType;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ConflictException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.*;
import com.vehiclerental.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AvailabilityAlertRepository availabilityAlertRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> getVehicles(
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
            String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Vehicle> vehiclePage = vehicleRepository.findVehiclesWithFilters(
                (search != null && !search.isBlank()) ? search.trim() : null,
                vehicleType,
                (fuelType != null && !fuelType.isBlank()) ? fuelType.trim() : null,
                (transmission != null && !transmission.isBlank()) ? transmission.trim() : null,
                (location != null && !location.isBlank()) ? location.trim() : null,
                minPrice,
                maxPrice,
                seats,
                status,
                pageable
        );

        return vehiclePage.map(this::mapToVehicleResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        return mapToVehicleResponse(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDetailsResponse getVehicleDetails(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

        List<ReviewResponse> reviews = reviewRepository.findByVehicleIdAndIsHiddenFalseOrderByCreatedAtDesc(id)
                .stream()
                .limit(5)
                .map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .vehicleId(r.getVehicle().getId())
                        .vehicleName(r.getVehicle().getBrand() + " " + r.getVehicle().getModel())
                        .userId(r.getUser().getId())
                        .userName(r.getUser().getFullName())
                        .userPhoto(r.getUser().getProfilePhoto())
                        .bookingId(r.getBooking().getId())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .isHidden(r.getIsHidden())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        boolean isAvailableNow = vehicle.getStatus() == VehicleStatus.AVAILABLE;

        return VehicleDetailsResponse.builder()
                .vehicle(mapToVehicleResponse(vehicle))
                .recentReviews(reviews)
                .isAvailableNow(isAvailableNow)
                .activeBookingsCount(0L)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getFeaturedVehicles() {
        return vehicleRepository.findByFeaturedTrue().stream()
                .map(this::mapToVehicleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(Long vehicleId, LocalDateTime pickupDate, LocalDateTime returnDate) {
        if (pickupDate.isAfter(returnDate) || pickupDate.isEqual(returnDate)) {
            throw new BadRequestException("Return date must be strictly after pickup date");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));

        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE || vehicle.getStatus() == VehicleStatus.OUT_OF_SERVICE) {
            return false;
        }

        Long overlapping = bookingRepository.countOverlappingBookings(vehicleId, pickupDate, returnDate, null);
        return overlapping == 0;
    }

    @Override
    @Transactional
    public VehicleResponse createVehicle(VehicleRequest request) {
        if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new ConflictException("A vehicle with registration number " + request.getRegistrationNumber() + " already exists");
        }

        Vehicle vehicle = Vehicle.builder()
                .brand(request.getBrand().trim())
                .model(request.getModel().trim())
                .year(request.getYear())
                .vehicleType(request.getVehicleType())
                .registrationNumber(request.getRegistrationNumber().trim().toUpperCase())
                .description(request.getDescription())
                .pricePerDay(request.getPricePerDay())
                .securityDeposit(request.getSecurityDeposit())
                .fuelType(request.getFuelType())
                .transmission(request.getTransmission())
                .seatingCapacity(request.getSeatingCapacity())
                .location(request.getLocation().trim())
                .status(request.getStatus() != null ? request.getStatus() : VehicleStatus.AVAILABLE)
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .rating(BigDecimal.valueOf(5.0))
                .totalReviews(0)
                .images(new ArrayList<>())
                .build();

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            boolean isFirst = true;
            for (String url : request.getImageUrls()) {
                if (url != null && !url.isBlank()) {
                    vehicle.getImages().add(VehicleImage.builder()
                            .vehicle(vehicle)
                            .imageUrl(url.trim())
                            .isPrimary(isFirst)
                            .build());
                    isFirst = false;
                }
            }
        }

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToVehicleResponse(saved);
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

        vehicle.setBrand(request.getBrand().trim());
        vehicle.setModel(request.getModel().trim());
        vehicle.setYear(request.getYear());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setDescription(request.getDescription());
        vehicle.setPricePerDay(request.getPricePerDay());
        vehicle.setSecurityDeposit(request.getSecurityDeposit());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setSeatingCapacity(request.getSeatingCapacity());
        vehicle.setLocation(request.getLocation().trim());

        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }
        if (request.getFeatured() != null) {
            vehicle.setFeatured(request.getFeatured());
        }

        if (request.getImageUrls() != null) {
            vehicle.getImages().clear();
            boolean isFirst = true;
            for (String url : request.getImageUrls()) {
                if (url != null && !url.isBlank()) {
                    vehicle.getImages().add(VehicleImage.builder()
                            .vehicle(vehicle)
                            .imageUrl(url.trim())
                            .isPrimary(isFirst)
                            .build());
                    isFirst = false;
                }
            }
        }

        Vehicle updated = vehicleRepository.save(vehicle);
        return mapToVehicleResponse(updated);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        vehicle.setStatus(VehicleStatus.OUT_OF_SERVICE);
        vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> compareVehicles(List<Long> vehicleIds) {
        if (vehicleIds == null || vehicleIds.size() < 2 || vehicleIds.size() > 3) {
            throw new BadRequestException("You can compare 2 or 3 vehicles side by side");
        }
        return vehicleRepository.findAllById(vehicleIds).stream()
                .map(this::mapToVehicleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void subscribeAvailabilityAlert(Long vehicleId, String userEmail) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        if (!availabilityAlertRepository.existsByVehicleIdAndUserIdAndNotifiedFalse(vehicleId, user.getId())) {
            availabilityAlertRepository.save(AvailabilityAlert.builder()
                    .vehicle(vehicle)
                    .user(user)
                    .notified(false)
                    .build());
        }
    }

    public VehicleResponse mapToVehicleResponse(Vehicle vehicle) {
        List<String> imageUrls = vehicle.getImages().stream()
                .map(VehicleImage::getImageUrl)
                .collect(Collectors.toList());

        String primaryImageUrl = vehicle.getImages().stream()
                .filter(VehicleImage::getIsPrimary)
                .map(VehicleImage::getImageUrl)
                .findFirst()
                .orElse(imageUrls.isEmpty() ? null : imageUrls.get(0));

        return VehicleResponse.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .vehicleType(vehicle.getVehicleType())
                .registrationNumber(vehicle.getRegistrationNumber())
                .description(vehicle.getDescription())
                .pricePerDay(vehicle.getPricePerDay())
                .securityDeposit(vehicle.getSecurityDeposit())
                .fuelType(vehicle.getFuelType())
                .transmission(vehicle.getTransmission())
                .seatingCapacity(vehicle.getSeatingCapacity())
                .location(vehicle.getLocation())
                .status(vehicle.getStatus())
                .rating(vehicle.getRating())
                .totalReviews(vehicle.getTotalReviews())
                .featured(vehicle.getFeatured())
                .primaryImageUrl(primaryImageUrl)
                .imageUrls(imageUrls)
                .build();
    }

    public VehicleServiceImpl(VehicleRepository vehicleRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository, UserRepository userRepository, AvailabilityAlertRepository availabilityAlertRepository) {
        this.vehicleRepository = vehicleRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.availabilityAlertRepository = availabilityAlertRepository;
    }
}
