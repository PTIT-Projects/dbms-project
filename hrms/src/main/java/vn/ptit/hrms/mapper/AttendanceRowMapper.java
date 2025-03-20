package vn.ptit.hrms.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.AttendanceStatus;
import vn.ptit.hrms.dao.EmployeeDAO;
import vn.ptit.hrms.domain.Attendance;
import vn.ptit.hrms.domain.Employee;

public class AttendanceRowMapper implements RowMapper<Attendance> {

    private final EmployeeDAO employeeDAO;

    public AttendanceRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Attendance attendance = new Attendance();

        // Map Attendance id
        attendance.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        Employee employee = employeeDAO.findById(employeeId);
        attendance.setEmployee(employee);

        // Map LocalDate field from SQL Date
        Date sqlDate = rs.getDate("date");
        if (sqlDate != null) {
            LocalDate localDate = sqlDate.toLocalDate();
            attendance.setDate(localDate);
        }

        // Map LocalTime fields from SQL Time for check-in and check-out times
        Time sqlCheckIn = rs.getTime("check_in_time");
        if (sqlCheckIn != null) {
            LocalTime checkInTime = sqlCheckIn.toLocalTime();
            attendance.setCheckInTime(checkInTime);
        }

        Time sqlCheckOut = rs.getTime("check_out_time");
        if (sqlCheckOut != null) {
            LocalTime checkOutTime = sqlCheckOut.toLocalTime();
            attendance.setCheckOutTime(checkOutTime);
        }

        // Map AttendanceStatus field using a helper method.
        String statusValue = rs.getString("status");
        if (statusValue != null) {
            attendance.setStatus(getAttendanceStatus(statusValue));
        }

        return attendance;
    }

    /**
     * Converts a string value from the database into the corresponding AttendanceStatus enum.
     */
    private AttendanceStatus getAttendanceStatus(String value) {
        for (AttendanceStatus status : AttendanceStatus.values()) {
            // Adjust the comparison if your enum uses a custom value
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown attendance status value: " + value);
    }
}
```