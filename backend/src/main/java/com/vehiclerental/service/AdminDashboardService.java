package com.vehiclerental.service;

import com.vehiclerental.dto.response.DashboardAnalyticsResponse;
import com.vehiclerental.dto.response.DashboardStatsResponse;
import com.vehiclerental.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface AdminDashboardService {
    DashboardStatsResponse getDashboardStats();
    DashboardAnalyticsResponse getDashboardAnalytics();
    Page<UserResponse> getAllUsers(String search, int page, int size);
    UserResponse toggleUserStatus(Long userId);
}
