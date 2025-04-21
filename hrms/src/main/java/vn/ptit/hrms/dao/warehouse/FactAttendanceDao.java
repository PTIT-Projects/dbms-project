package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.FactAttendance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class FactAttendanceDao {
    private final JdbcTemplate jdbcTemplate;

    public FactAttendanceDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FactAttendance> getAttendanceByEmployeeSk(Integer employeeSk) {
        String sql = "SELECT * FROM fact_attendance WHERE employee_sk = ?";
        return jdbcTemplate.query(sql, new FactAttendanceRowMapper(), employeeSk);
    }

    public List<FactAttendance> getAttendanceByEmployeeAndDateRange(Integer employeeSk, Integer startDateSk, Integer endDateSk) {
        String sql = "SELECT * FROM fact_attendance WHERE employee_sk = ? AND date_sk BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new FactAttendanceRowMapper(), employeeSk, startDateSk, endDateSk);
    }

    public List<FactAttendance> getAttendanceByDateSk(Integer dateSk) {
        String sql = "SELECT * FROM fact_attendance WHERE date_sk = ?";
        return jdbcTemplate.query(sql, new FactAttendanceRowMapper(), dateSk);
    }

    public List<FactAttendance> getAttendanceByDateRange(Integer startDateSk, Integer endDateSk) {
        String sql = "SELECT * FROM fact_attendance WHERE date_sk BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new FactAttendanceRowMapper(), startDateSk, endDateSk);
    }

    public List<Map<String, Object>> getLateEmployeesByDepartment(Integer departmentSk, Integer startDateSk, Integer endDateSk) {
        String sql = "SELECT department_name, COUNT(*) as late_count FROM fact_attendance WHERE department_sk = ? AND date_sk BETWEEN ? AND ? AND is_late = 1 GROUP BY department_name";
        return jdbcTemplate.queryForList(sql, departmentSk, startDateSk, endDateSk);
    }

    public List<Map<String, Object>> getOvertimeStatsByDepartment(Integer departmentSk, Integer startDateSk, Integer endDateSk) {
        String sql = "SELECT department_name, SUM(overtime_hours) as total_overtime FROM fact_attendance WHERE department_sk = ? AND date_sk BETWEEN ? AND ? GROUP BY department_name";
        return jdbcTemplate.queryForList(sql, departmentSk, startDateSk, endDateSk);
    }

    public Double getAverageHoursWorkedByEmployee(Integer employeeSk, Integer startDateSk, Integer endDateSk) {
        String sql = "SELECT AVG(hours_worked) FROM fact_attendance WHERE employee_sk = ? AND date_sk BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, Double.class, employeeSk, startDateSk, endDateSk);
    }

    private static class FactAttendanceRowMapper implements RowMapper<FactAttendance> {
        @Override
        public FactAttendance mapRow(ResultSet rs, int rowNum) throws SQLException {
            FactAttendance attendance = new FactAttendance();
            attendance.setAttendanceSk(rs.getInt("attendance_sk"));
            attendance.setAttendanceId(rs.getInt("attendance_id"));
            attendance.setEmployeeSk(rs.getInt("employee_sk"));
            attendance.setEmployeeName(rs.getString("employee_name"));
            attendance.setDepartmentName(rs.getString("department_name"));
            attendance.setPositionName(rs.getString("position_name"));
            attendance.setDateSk(rs.getInt("date_sk"));
            attendance.setCheckInTime(rs.getTime("check_in_time") != null ? rs.getTime("check_in_time").toLocalTime() : null);
            attendance.setCheckOutTime(rs.getTime("check_out_time") != null ? rs.getTime("check_out_time").toLocalTime() : null);
            attendance.setHoursWorked(rs.getBigDecimal("hours_worked"));
            attendance.setIsLate(rs.getBoolean("is_late"));
            attendance.setOvertimeHours(rs.getBigDecimal("overtime_hours"));
            attendance.setIsEarlyLeave(rs.getBoolean("is_early_leave"));
            attendance.setStatus(rs.getString("status"));
            return attendance;
        }
    }
}