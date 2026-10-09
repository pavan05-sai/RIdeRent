package com.vehiclerental.service.impl;

import com.vehiclerental.dto.response.DashboardAnalyticsResponse;
import com.vehiclerental.dto.response.DashboardStatsResponse;
import com.vehiclerental.dto.response.UserResponse;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.enums.BookingStatus;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.MaintenanceRecordRepository;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.repository.VehicleRepository;
import com.vehiclerental.service.AdminDashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final MaintenanceRecordRepository maintenanceRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalVehicles = vehicleRepository.count();
        Long activeRentals = bookingRepository.countByStatus(BookingStatus.ACTIVE);
        Long upcomingBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        Long completedRentals = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        Long cancelledRentals = bookingRepository.countByStatus(BookingStatus.CANCELLED);
        BigDecimal totalRevenue = bookingRepository.calculateTotalRevenue();
        Long vehiclesUnderMaintenance = vehicleRepository.countByStatus(VehicleStatus.MAINTENANCE);

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalVehicles(totalVehicles)
                .activeRentals(activeRentals)
                .upcomingBookings(upcomingBookings)
                .completedRentals(completedRentals)
                .cancelledRentals(cancelledRentals)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .vehiclesUnderMaintenance(vehiclesUnderMaintenance)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardAnalyticsResponse getDashboardAnalytics() {
        // Monthly revenue
        List<Object[]> monthlyRaw = bookingRepository.getMonthlyRevenue();
        List<DashboardAnalyticsResponse.MonthlyRevenueItem> monthlyRevenue = new ArrayList<>();
        if (monthlyRaw != null && !monthlyRaw.isEmpty()) {
            for (Object[] row : monthlyRaw) {
                String monthLabel = "Month " + row[0] + "/" + row[1];
                BigDecimal rev = row[2] != null ? (BigDecimal) row[2] : BigDecimal.ZERO;
                monthlyRevenue.add(new DashboardAnalyticsResponse.MonthlyRevenueItem(monthLabel, rev));
            }
        } else {
            monthlyRevenue.add(new DashboardAnalyticsResponse.MonthlyRevenueItem("Current Month", bookingRepository.calculateTotalRevenue()));
        }

        // Category distribution
        List<Object[]> categoryRaw = vehicleRepository.countVehiclesByCategory();
        List<DashboardAnalyticsResponse.CategoryDistributionItem> categoryDist = new ArrayList<>();
        if (categoryRaw != null) {
            for (Object[] row : categoryRaw) {
                categoryDist.add(new DashboardAnalyticsResponse.CategoryDistributionItem(
                        row[0].toString(),
                        ((Number) row[1]).longValue()
                ));
            }
        }

        // Most rented vehicles
        List<Object[]> mostRentedRaw = bookingRepository.getMostRentedVehicles();
        List<DashboardAnalyticsResponse.MostRentedItem> mostRented = new ArrayList<>();
        if (mostRentedRaw != null) {
            for (Object[] row : mostRentedRaw) {
                mostRented.add(new DashboardAnalyticsResponse.MostRentedItem(
                        row[0] + " " + row[1],
                        ((Number) row[2]).longValue()
                ));
            }
        }

        // Status counts
        Map<String, Long> statusMap = new HashMap<>();
        for (BookingStatus st : BookingStatus.values()) {
            statusMap.put(st.name(), bookingRepository.countByStatus(st));
        }

        return DashboardAnalyticsResponse.builder()
                .revenueByMonth(monthlyRevenue)
                .categoryDistribution(categoryDist)
                .mostRentedVehicles(mostRented)
                .bookingStatusDistribution(statusMap)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(String search, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        return userRepository.searchUsers(searchParam, pageable)
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .profilePhoto(u.getProfilePhoto())
                        .isActive(u.getIsActive())
                        .roles(u.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList()))
                        .createdAt(u.getCreatedAt())
                        .build());
    }

    @Override
    @Transactional
    public UserResponse toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setIsActive(!user.getIsActive());
        User saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .profilePhoto(saved.getProfilePhoto())
                .isActive(saved.getIsActive())
                .roles(saved.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList()))
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public AdminDashboardServiceImpl(UserRepository userRepository, VehicleRepository vehicleRepository, BookingRepository bookingRepository, MaintenanceRecordRepository maintenanceRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.bookingRepository = bookingRepository;
        this.maintenanceRepository = maintenanceRepository;
    }
}
