package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.AttendanceStatus;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.Attendance;
import vn.ptit.hrms.domain.Employee;

public class AttendanceRowMapper implements RowMapper<Attendance> {
    private final EmployeeDao employeeDAO;

    public AttendanceRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Attendance attendance = new Attendance();

        // Map Attendance id - use the correct column name AttendanceID
        attendance.setId(rs.getInt("AttendanceID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        attendance.setEmployee(employee);

        // Map LocalDate field from SQL Date - use the correct column name Date
        Date sqlDate = rs.getDate("Date");
        if (sqlDate != null) {
            LocalDate localDate = sqlDate.toLocalDate();
            attendance.setDate(localDate);
        }

        // Map LocalTime fields from SQL Time for check-in and check-out times
        // Use the correct column names CheckInTime and CheckOutTime
        Time sqlCheckIn = rs.getTime("CheckInTime");
        if (sqlCheckIn != null) {
            LocalTime checkInTime = sqlCheckIn.toLocalTime();
            attendance.setCheckInTime(checkInTime);
        }

        Time sqlCheckOut = rs.getTime("CheckOutTime");
        if (sqlCheckOut != null) {
            LocalTime checkOutTime = sqlCheckOut.toLocalTime();
            attendance.setCheckOutTime(checkOutTime);
        }

        // Map AttendanceStatus field using a helper method.
        String statusValue = rs.getString("Status");
        if (statusValue != null) {
            attendance.setStatus(getAttendanceStatus(statusValue));
        }

        return attendance;
    }

    private AttendanceStatus getAttendanceStatus(String value) {
        for (AttendanceStatus status : AttendanceStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return AttendanceStatus.PRESENT; // Default value
    }
}