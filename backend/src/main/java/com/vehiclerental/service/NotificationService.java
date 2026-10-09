package com.vehiclerental.service;

import com.vehiclerental.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<NotificationResponse> getUserNotifications(String userEmail, int page, int size);
    Long getUnreadCount(String userEmail);
    void markAsRead(Long id, String userEmail);
    void markAllAsRead(String userEmail);
}
