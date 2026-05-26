package com.uepbh.repository;

import com.uepbh.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOwnerId(String ownerId);
    List<Notification> findByOwnerIdAndUserId(String ownerId, Long userId);
    List<Notification> findByOwnerIdAndUserIdAndRead(String ownerId, Long userId, Boolean read);
}
