package vn.ptit.hrms.dao.warehouse;

import vn.ptit.hrms.dto.warehouse.LeaveStatsByDeptPositionDTO;
import vn.ptit.hrms.dto.warehouse.LeaveUsageEmployeeDTO;
import vn.ptit.hrms.dto.warehouse.LeaveTrendByMonthDTO;
import vn.ptit.hrms.dto.warehouse.LeaveTrendByQuarterDTO;
import vn.ptit.hrms.dto.warehouse.LeaveTypeStatsDTO;
import vn.ptit.hrms.dto.warehouse.LeaveByExperienceDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactLeaveBalanceDao {
    private final JdbcTemplate jdbcTemplate;

    public FactLeaveBalanceDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 3.a Số ngày nghỉ còn lại theo phòng ban và vị trí
    public List<LeaveStatsByDeptPositionDTO> getLeaveStatsByDeptPosition() {
        String sql = """
            SELECT 
                department_name,
                position_name,
                COUNT(DISTINCT employee_name) AS employee_count,
                AVG(total_leave_days) AS avg_total_leave,
                AVG(used_leave_days) AS avg_used_leave,
                AVG(remaining_leave_days) AS avg_remaining_leave,
                ROUND(AVG(used_leave_days * 100.0 / total_leave_days), 2) AS usage_percentage
            FROM fact_leave_balance
            WHERE granularity = 'Năm'
            GROUP BY department_name, position_name
            ORDER BY department_name, usage_percentage DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<LeaveStatsByDeptPositionDTO>() {
            @Override
            public LeaveStatsByDeptPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveStatsByDeptPositionDTO dto = new LeaveStatsByDeptPositionDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setEmployeeCount(rs.getInt("employee_count"));
                dto.setAvgTotalLeave(rs.getDouble("avg_total_leave"));
                dto.setAvgUsedLeave(rs.getDouble("avg_used_leave"));
                dto.setAvgRemainingLeave(rs.getDouble("avg_remaining_leave"));
                dto.setUsagePercentage(rs.getDouble("usage_percentage"));
                return dto;
            }
        });
    }

    // 3.b Nhân viên đã dùng hết hoặc gần hết ngày nghỉ
    public List<LeaveUsageEmployeeDTO> getEmployeesWithLowRemainingLeave(int maxRemainingDays) {
        String sql = """
            SELECT 
                employee_name,
                department_name,
                position_name,
                total_leave_days,
                used_leave_days,
                remaining_leave_days,
                ROUND(used_leave_days * 100.0 / total_leave_days, 2) AS usage_percentage
            FROM fact_leave_balance
            WHERE 
                granularity = 'Năm' 
                AND remaining_leave_days <= ?
            ORDER BY usage_percentage DESC
        """;
        return jdbcTemplate.query(sql, new Object[]{maxRemainingDays}, new RowMapper<LeaveUsageEmployeeDTO>() {
            @Override
            public LeaveUsageEmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveUsageEmployeeDTO dto = new LeaveUsageEmployeeDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setTotalLeaveDays(rs.getInt("total_leave_days"));
                dto.setUsedLeaveDays(rs.getInt("used_leave_days"));
                dto.setRemainingLeaveDays(rs.getInt("remaining_leave_days"));
                dto.setUsagePercentage(rs.getDouble("usage_percentage"));
                return dto;
            }
        });
    }

    // 3.c Xu hướng nghỉ phép theo tháng
    public List<LeaveTrendByMonthDTO> getLeaveTrendByMonth() {
        String sql = """
            SELECT 
                dd.year,
                dd.month,
                SUM(fl.used_leave_days) AS total_leave_days_used,
                COUNT(DISTINCT fl.employee_name) AS employees_took_leave
            FROM fact_leave_balance fl
            JOIN dim_date dd ON fl.date_sk = dd.date_sk
            WHERE granularity = 'Năm'
            GROUP BY dd.year, dd.month
            ORDER BY dd.year, dd.month
        """;
        return jdbcTemplate.query(sql, new RowMapper<LeaveTrendByMonthDTO>() {
            @Override
            public LeaveTrendByMonthDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveTrendByMonthDTO dto = new LeaveTrendByMonthDTO();
                dto.setYear(rs.getInt("year"));
                dto.setMonth(rs.getInt("month"));
                dto.setTotalLeaveDaysUsed(rs.getInt("total_leave_days_used"));
                dto.setEmployeesTookLeave(rs.getInt("employees_took_leave"));
                return dto;
            }
        });
    }

    // 3.c Xu hướng nghỉ phép theo quý
    public List<LeaveTrendByQuarterDTO> getLeaveTrendByQuarter() {
        String sql = """
            SELECT 
                dd.year,
                dd.quarter,
                SUM(fl.used_leave_days) AS total_leave_days_used,
                ROUND(AVG(fl.used_leave_days), 2) AS avg_leave_per_employee
            FROM fact_leave_balance fl
            JOIN dim_date dd ON fl.date_sk = dd.date_sk
            WHERE granularity = 'Năm'
            GROUP BY dd.year, dd.quarter
            ORDER BY dd.year, dd.quarter
        """;
        return jdbcTemplate.query(sql, new RowMapper<LeaveTrendByQuarterDTO>() {
            @Override
            public LeaveTrendByQuarterDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveTrendByQuarterDTO dto = new LeaveTrendByQuarterDTO();
                dto.setYear(rs.getInt("year"));
                dto.setQuarter(rs.getInt("quarter"));
                dto.setTotalLeaveDaysUsed(rs.getInt("total_leave_days_used"));
                dto.setAvgLeavePerEmployee(rs.getDouble("avg_leave_per_employee"));
                return dto;
            }
        });
    }

    // 3.d Phân tích loại nghỉ phép được sử dụng nhiều nhất
    public List<LeaveTypeStatsDTO> getLeaveTypeStats() {
        String sql = """
            SELECT 
                leave_type,
                SUM(used_leave_days) AS total_days_used,
                COUNT(DISTINCT employee_sk) AS total_employees_used
            FROM fact_leave_balance
            GROUP BY leave_type
            ORDER BY total_days_used DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<LeaveTypeStatsDTO>() {
            @Override
            public LeaveTypeStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveTypeStatsDTO dto = new LeaveTypeStatsDTO();
                dto.setLeaveType(rs.getString("leave_type"));
                dto.setTotalDaysUsed(rs.getInt("total_days_used"));
                dto.setTotalEmployeesUsed(rs.getInt("total_employees_used"));
                return dto;
            }
        });
    }

    // 3.e Phân tích nghỉ phép theo thâm niên
    public List<LeaveByExperienceDTO> getLeaveByExperience() {
        String sql = """
            SELECT 
                CASE 
                    WHEN de.total_years_worked < 1 THEN 'Dưới 1 năm'
                    WHEN de.total_years_worked BETWEEN 1 AND 3 THEN '1-3 năm'
                    WHEN de.total_years_worked BETWEEN 3 AND 5 THEN '3-5 năm'
                    WHEN de.total_years_worked > 5 THEN 'Trên 5 năm'
                END AS experience_level,
                AVG(fl.total_leave_days) AS avg_allocated_leave,
                AVG(fl.used_leave_days) AS avg_used_leave,
                ROUND(AVG(fl.used_leave_days * 100.0 / fl.total_leave_days), 2) AS usage_percentage
            FROM fact_leave_balance fl
            JOIN dim_employees de ON fl.employee_name = de.full_name
            WHERE fl.granularity = 'Năm'
            GROUP BY 
                CASE 
                    WHEN de.total_years_worked < 1 THEN 'Dưới 1 năm'
                    WHEN de.total_years_worked BETWEEN 1 AND 3 THEN '1-3 năm'
                    WHEN de.total_years_worked BETWEEN 3 AND 5 THEN '3-5 năm'
                    WHEN de.total_years_worked > 5 THEN 'Trên 5 năm'
                END
            ORDER BY experience_level
        """;
        return jdbcTemplate.query(sql, new RowMapper<LeaveByExperienceDTO>() {
            @Override
            public LeaveByExperienceDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                LeaveByExperienceDTO dto = new LeaveByExperienceDTO();
                dto.setExperienceLevel(rs.getString("experience_level"));
                dto.setAvgAllocatedLeave(rs.getDouble("avg_allocated_leave"));
                dto.setAvgUsedLeave(rs.getDouble("avg_used_leave"));
                dto.setUsagePercentage(rs.getDouble("usage_percentage"));
                return dto;
            }
        });
    }
}
