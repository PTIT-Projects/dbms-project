package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Attendance;
import vn.ptit.hrms.domain.Employee; // Assuming you have an Employee class
import vn.ptit.hrms.constant.AttendanceStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class AttendanceDao {
    private final JdbcTemplate jdbcTemplate;

    public AttendanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to create a new attendance record
    public void createAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance (employee_id, date, check_in_time, check_out_time, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, attendance.getEmployee().getId(), attendance.getDate(), attendance.getCheckInTime(), attendance.getCheckOutTime(), attendance.getStatus().name());
    }

    // Method to get an attendance record by ID
    public Attendance getAttendanceById(Integer id) {
        String sql = "SELECT * FROM attendance WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new AttendanceRowMapper(), id);
    }

    // Method to get all attendance records
    public List<Attendance> getAllAttendance() {
        String sql = "SELECT * FROM attendance";
        return jdbcTemplate.query(sql, new AttendanceRowMapper());
    }

    // Method to update an attendance record
    public void updateAttendance(Attendance attendance) {
        String sql = "UPDATE attendance SET employee_id = ?, date = ?, check_in_time = ?, check_out_time = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, attendance.getEmployee().getId(), attendance.getDate(), attendance.getCheckInTime(), attendance.getCheckOutTime(), attendance.getStatus().name(), attendance.getId());
    }

    // Method to delete an attendance record
    public void deleteAttendance(Integer id) {
        String sql = "DELETE FROM attendance WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMapper for Attendance
    private static class AttendanceRowMapper implements RowMapper<Attendance> {
        @Override
        public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
            Attendance attendance = new Attendance();
            attendance.setId(rs.getInt("id"));

            // Assuming you have a method to fetch Employee by ID
            Employee employee = new Employee(); // Replace with actual fetching logic
            employee.setId(rs.getInt("employee_id"));
            attendance.setEmployee(employee);

            attendance.setDate(rs.getDate("date").toLocalDate());
            attendance.setCheckInTime(rs.getTime("check_in_time").toLocalTime());
            attendance.setCheckOutTime(rs.getTime("check_out_time").toLocalTime());

            String statusValue = rs.getString("status");
            if (statusValue != null) {
                attendance.setStatus(AttendanceStatus.valueOf(statusValue));
            }

            return attendance;
        }
    }
}
