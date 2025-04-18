package vn.ptit.hrms.dao.warehouse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AttendanceFactDao {
    private final JdbcTemplate jdbcTemplate;

    public AttendanceFactDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAverageHoursByDepartment(int year, int month) {
        String sql = "SELECT dd.department_name, AVG(fa.hours_worked) as avg_hours " +
                "FROM fact_attendance fa " +
                "JOIN dim_employee de ON fa.employee_key = de.employee_key " +
                "JOIN dim_department dd ON fa.department_key = dd.department_key " +
                "JOIN dim_time dt ON fa.time_key = dt.time_id " +
                "WHERE dt.year_num = ? AND dt.month_num = ? " +
                "GROUP BY dd.department_name";

        return jdbcTemplate.queryForList(sql, year, month);
    }

    public List<Map<String, Object>> getAttendanceStatusDistribution(int year, int month) {
        String sql = "SELECT fa.attendance_status, COUNT(*) as count " +
                "FROM fact_attendance fa " +
                "JOIN dim_time dt ON fa.time_key = dt.time_id " +
                "WHERE dt.year_num = ? AND dt.month_num = ? " +
                "GROUP BY fa.attendance_status";

        return jdbcTemplate.queryForList(sql, year, month);
    }
}