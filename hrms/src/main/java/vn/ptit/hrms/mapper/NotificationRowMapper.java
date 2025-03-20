package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Notification;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee

public class NotificationRowMapper implements RowMapper<Notification> {

    private final EmployeeDAO employeeDAO;

    public NotificationRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();

        // Map Notification id
        notification.setId(rs.getInt("id"));

        // Map Notification title
        notification.setTitle(rs.getString("title"));

        // Map Notification content
        notification.setContent(rs.getString("content"));

        // Map LocalDateTime field from SQL Timestamp for created date
        Timestamp sqlCreatedDate = rs.getTimestamp("created_date");
        if (sqlCreatedDate != null) {
            notification.setCreatedDate(sqlCreatedDate.toLocalDateTime());
        }

        // Retrieve created by employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int createdById = rs.getInt("created_by");
        if (createdById > 0) { // Assuming employee ID is positive
            Employee createdBy = employeeDAO.findById(createdById);
            notification.setCreatedBy(createdBy);
        }

        return notification;
    }
}
