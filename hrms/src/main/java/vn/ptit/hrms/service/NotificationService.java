package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.NotificationDao;

@Service
public class NotificationService {
    private final NotificationDao notificationDao;

    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }
}
