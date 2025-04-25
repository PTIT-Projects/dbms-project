package vn.ptit.hrms.dao.warehouse;

import vn.ptit.hrms.dto.warehouse.EmployeeWorkSummaryDTO;
import vn.ptit.hrms.dto.warehouse.DepartmentLateStatsDTO;
import vn.ptit.hrms.dto.warehouse.EmployeeOvertimeDTO;
import vn.ptit.hrms.dto.warehouse.DepartmentPerformanceDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactAttendanceDao {
    private final JdbcTemplate jdbcTemplate;

    public FactAttendanceDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1.a Tổng số giờ làm việc của mỗi nhân viên trong năm
    public List<EmployeeWorkSummaryDTO> getEmployeeWorkSummaryByYear(int year) {
        String sql = """
            SELECT 
                employee_name,
                department_name,
                SUM(hours_worked) AS total_hours_worked,
                COUNT(*) AS working_days
            FROM fact_attendance fa
            INNER JOIN dim_date dd ON fa.date_sk = dd.date_sk
            WHERE dd.year = ?
            GROUP BY employee_name, department_name
            ORDER BY total_hours_worked DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<EmployeeWorkSummaryDTO>() {
            @Override
            public EmployeeWorkSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmployeeWorkSummaryDTO dto = new EmployeeWorkSummaryDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setTotalHoursWorked(rs.getBigDecimal("total_hours_worked"));
                dto.setWorkingDays(rs.getInt("working_days"));
                return dto;
            }
        }, year);
    }

    // 1.b Phòng ban có tỷ lệ đi muộn cao nhất
    // Fix for SQL syntax error in getTopLateDepartments method
    public List<DepartmentLateStatsDTO> getTopLateDepartments(int limit) {
        // Instead of using TOP ? with a parameter placeholder
        String sql = String.format("""
        SELECT TOP %d 
            department_name,
            COUNT(*) AS total_days,
            SUM(CAST(is_late AS INT)) AS late_days,
            ROUND(SUM(CAST(is_late AS FLOAT)) * 100 / COUNT(*), 2) AS late_percentage
        FROM fact_attendance
        GROUP BY department_name
        ORDER BY late_percentage DESC
    """, limit);

        // Now the query doesn't need the limit parameter
        return jdbcTemplate.query(sql, new RowMapper<DepartmentLateStatsDTO>() {
            @Override
            public DepartmentLateStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                DepartmentLateStatsDTO dto = new DepartmentLateStatsDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setTotalDays(rs.getInt("total_days"));
                dto.setLateDays(rs.getInt("late_days"));
                dto.setLatePercentage(rs.getDouble("late_percentage"));
                return dto;
            }
        });
    }

    // 1.c Top nhân viên có tổng giờ làm thêm cao nhất
    public List<EmployeeOvertimeDTO> getTopEmployeesByOvertime(int limit) {
        String sql = String.format("""
            SELECT TOP %d 
                employee_name,
                department_name,
                SUM(overtime_hours) AS total_overtime,
                ROUND(AVG(overtime_hours), 2) AS avg_overtime_per_day
            FROM fact_attendance
            WHERE overtime_hours > 0
            GROUP BY employee_name, department_name
            ORDER BY total_overtime DESC
        """, limit
        );
        return jdbcTemplate.query(sql, new RowMapper<EmployeeOvertimeDTO>() {
            @Override
            public EmployeeOvertimeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmployeeOvertimeDTO dto = new EmployeeOvertimeDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setTotalOvertime(rs.getBigDecimal("total_overtime"));
                dto.setAvgOvertimePerDay(rs.getBigDecimal("avg_overtime_per_day"));
                return dto;
            }
        });
    }

    // 1.d Thống kê hiệu suất theo phòng ban
    public List<DepartmentPerformanceDTO> getDepartmentPerformanceStats() {
        String sql = """
            SELECT 
                department_name,
                COUNT(DISTINCT employee_name) AS employee_count,
                ROUND(AVG(hours_worked), 2) AS avg_hours_per_day,
                SUM(CAST(is_late AS INT)) * 100.0 / COUNT(*) AS late_percentage,
                SUM(overtime_hours) AS total_overtime
            FROM fact_attendance
            GROUP BY department_name
            ORDER BY avg_hours_per_day DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<DepartmentPerformanceDTO>() {
            @Override
            public DepartmentPerformanceDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                DepartmentPerformanceDTO dto = new DepartmentPerformanceDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setEmployeeCount(rs.getInt("employee_count"));
                dto.setAvgHoursPerDay(rs.getBigDecimal("avg_hours_per_day"));
                dto.setLatePercentage(rs.getDouble("late_percentage"));
                dto.setTotalOvertime(rs.getBigDecimal("total_overtime"));
                return dto;
            }
        });
    }
}
