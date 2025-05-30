package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.AttendanceStatusEnum;
import vn.ptit.hrms.dao.primary.EmployeeDao;
import vn.ptit.hrms.domain.primary.Attendance;
import vn.ptit.hrms.domain.primary.Employee;

public class AttendanceRowMapper implements RowMapper<Attendance> {
    private final EmployeeDao employeeDAO;

    public AttendanceRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Attendance attendance = new Attendance();

       
        attendance.setId(rs.getInt("AttendanceID"));

       
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        attendance.setEmployee(employee);

       
        Date sqlDate = rs.getDate("Date");
        if (sqlDate != null) {
            LocalDate localDate = sqlDate.toLocalDate();
            attendance.setDate(localDate);
        }

       
       
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

       
        String statusValue = rs.getNString("Status");
        if (statusValue != null) {
            attendance.setStatus(getAttendanceStatus(statusValue));
        }

        return attendance;
    }

    private AttendanceStatusEnum getAttendanceStatus(String value) {
        for (AttendanceStatusEnum status : AttendanceStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return AttendanceStatusEnum.PRESENT;
    }
}