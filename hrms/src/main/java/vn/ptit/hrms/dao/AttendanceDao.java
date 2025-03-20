package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Attendance;
import vn.ptit.hrms.mapper.AttendanceRowMapper;

import java.util.List;

@Repository
public class AttendanceDao {
    private final JdbcTemplate jdbcTemplate;
    private final AttendanceRowMapper attendanceRowMapper;

    public AttendanceDao(JdbcTemplate jdbcTemplate, AttendanceRowMapper attendanceRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.attendanceRowMapper = attendanceRowMapper;
    }

    public void createAttendance(Attendance attendance) {
        String sql = "INSERT INTO Attendance (EmployeeID, Date, CheckInTime, CheckOutTime, Status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                attendance.getEmployee().getId(),
                attendance.getDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getStatus().getValue());
    }

    public Attendance getAttendanceById(Integer id) {
        String sql = "SELECT * FROM Attendance WHERE AttendanceID = ?";
        return jdbcTemplate.queryForObject(sql, attendanceRowMapper, id);
    }

    public List<Attendance> getAllAttendance() {
        String sql = "SELECT * FROM Attendance";
        return jdbcTemplate.query(sql, attendanceRowMapper);
    }

    public void updateAttendance(Attendance attendance) {
        String sql = "UPDATE Attendance SET EmployeeID = ?, Date = ?, CheckInTime = ?, CheckOutTime = ?, Status = ? WHERE AttendanceID = ?";
        jdbcTemplate.update(sql,
                attendance.getEmployee().getId(),
                attendance.getDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getStatus().getValue(),
                attendance.getId());
    }

    public void deleteAttendance(Integer id) {
        String sql = "DELETE FROM Attendance WHERE AttendanceID = ?";
        jdbcTemplate.update(sql, id);
    }
}