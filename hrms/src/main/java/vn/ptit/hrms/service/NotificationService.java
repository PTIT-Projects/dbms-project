package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.NotificationDao;
import vn.ptit.hrms.domain.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    public final NotificationDao notificationDao;

    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void createNotification(Notification notification) {
        if (notification.getCreatedDate() == null) {
            notification.setCreatedDate(LocalDateTime.now());
        }
        notificationDao.createNotification(notification);
    }

    public Notification getNotificationById(Integer id) {
        return notificationDao.getNotificationById(id);
    }

    public List<Notification> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }

    public List<Notification> getRecentNotifications(int limit) {
        List<Notification> allNotifications = notificationDao.getAllNotifications();

        return allNotifications.stream()
                .sorted((n1, n2) -> n2.getCreatedDate().compareTo(n1.getCreatedDate()))
                .limit(limit)
                .toList();
    }

    public List<Notification> getNotificationsByCreator(Integer creatorId) {
        List<Notification> allNotifications = notificationDao.getAllNotifications();

        return allNotifications.stream()
                .filter(notification -> notification.getCreatedBy() != null &&
                        notification.getCreatedBy().getId().equals(creatorId))
                .toList();
    }

    public void updateNotification(Notification notification) {
        notificationDao.updateNotification(notification);
    }

    public void deleteNotification(Integer id) {
        notificationDao.deleteNotification(id);
    }
}