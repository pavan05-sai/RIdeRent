package com.vehiclerental.service.impl;

import com.vehiclerental.dto.response.NotificationResponse;
import com.vehiclerental.entity.Notification;
import com.vehiclerental.entity.User;
import com.vehiclerental.exception.ForbiddenException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.NotificationRepository;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(this::mapToNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) return 0L;
        return notificationRepository.countByUserIdAndIsReadFalse(user.getId());
    }

    @Override
    @Transactional
    public void markAsRead(Long id, String userEmail) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Unauthorized to modify this notification");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        notificationRepository.markAllAsRead(user.getId());
    }

    private NotificationResponse mapToNotificationResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }
}
