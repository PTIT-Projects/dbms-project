package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Notification;
import vn.ptit.hrms.dao.EmployeeDao;

public class NotificationRowMapper implements RowMapper<Notification> {

    private final EmployeeDao employeeDAO;

    public NotificationRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();

       
        notification.setId(rs.getInt("NotificationID"));

       
        notification.setTitle(rs.getString("Title"));

       
        notification.setContent(rs.getString("Content"));

       
        Timestamp sqlCreatedDate = rs.getTimestamp("CreatedDate");
        if (sqlCreatedDate != null) {
            notification.setCreatedDate(sqlCreatedDate.toLocalDateTime());
        }

       
        int createdById = rs.getInt("CreatedBy");
        if (createdById > 0) {
            Employee createdBy = employeeDAO.getEmployeeById(createdById);
            notification.setCreatedBy(createdBy);
        }

        return notification;
    }
}