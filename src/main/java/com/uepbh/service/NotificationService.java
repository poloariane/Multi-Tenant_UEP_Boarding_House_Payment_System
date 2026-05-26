package com.uepbh.service;

import com.uepbh.dto.NotificationDTO;
import com.uepbh.entity.Notification;
import com.uepbh.repository.NotificationRepository;
import com.uepbh.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationDTO sendNotification(Long userId, String title, String message, String type) {
        String ownerId = TenantContext.getCurrentTenant();
        Notification notification = new Notification();
        notification.setOwnerId(ownerId);
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.valueOf(type));
        notification.setRead(false);

        Notification saved = notificationRepository.save(notification);
        return convertToDTO(saved);
    }

    public List<NotificationDTO> getUserNotifications(Long userId) {
        String ownerId = TenantContext.getCurrentTenant();
        return notificationRepository.findByOwnerIdAndUserId(ownerId, userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        String ownerId = TenantContext.getCurrentTenant();
        return notificationRepository.findByOwnerIdAndUserIdAndRead(ownerId, userId, false)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        String ownerId = TenantContext.getCurrentTenant();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        String ownerId = TenantContext.getCurrentTenant();
        List<Notification> notifications = notificationRepository.findByOwnerIdAndUserIdAndRead(ownerId, userId, false);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType().toString());
        dto.setRead(notification.getRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
