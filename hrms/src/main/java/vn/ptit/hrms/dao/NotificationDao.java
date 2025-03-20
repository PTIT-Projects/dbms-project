package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Notification;
import vn.ptit.hrms.mapper.NotificationRowMapper;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class NotificationDao {
    private final JdbcTemplate jdbcTemplate;
    private final NotificationRowMapper notificationRowMapper;

    public NotificationDao(JdbcTemplate jdbcTemplate, NotificationRowMapper notificationRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.notificationRowMapper = notificationRowMapper;
    }

    // Method to create a new notification
    public void createNotification(Notification notification) {
        String sql = "INSERT INTO Notifications (Title, Content, CreatedDate, CreatedBy) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                notification.getTitle(),
                notification.getContent(),
                notification.getCreatedDate() != null ? Timestamp.valueOf(notification.getCreatedDate()) : null,
                notification.getCreatedBy() != null ? notification.getCreatedBy().getId() : null);
    }

    // Method to get a notification by ID
    public Notification getNotificationById(Integer id) {
        String sql = "SELECT * FROM Notifications WHERE NotificationID = ?";
        return jdbcTemplate.queryForObject(sql, notificationRowMapper, id);
    }

    // Method to get all notifications
    public List<Notification> getAllNotifications() {
        String sql = "SELECT * FROM Notifications";
        return jdbcTemplate.query(sql, notificationRowMapper);
    }

    // Method to update a notification
    public void updateNotification(Notification notification) {
        String sql = "UPDATE Notifications SET Title = ?, Content = ?, CreatedDate = ?, CreatedBy = ? WHERE NotificationID = ?";
        jdbcTemplate.update(sql,
                notification.getTitle(),
                notification.getContent(),
                notification.getCreatedDate() != null ? Timestamp.valueOf(notification.getCreatedDate()) : null,
                notification.getCreatedBy() != null ? notification.getCreatedBy().getId() : null,
                notification.getId());
    }

    // Method to delete a notification
    public void deleteNotification(Integer id) {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        jdbcTemplate.update(sql, id);
    }
}