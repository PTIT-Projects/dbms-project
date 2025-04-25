package vn.ptit.hrms.dao.warehouse;

import vn.ptit.hrms.dto.warehouse.WorkTripStatsByEmployeeDTO;
import vn.ptit.hrms.dto.warehouse.WorkTripStatsByDestinationDTO;
import vn.ptit.hrms.dto.warehouse.WorkTripCostByDepartmentDTO;
import vn.ptit.hrms.dto.warehouse.WorkTripPurposeStatsDTO;
import vn.ptit.hrms.dto.warehouse.WorkTripTrendByMonthDTO;
import vn.ptit.hrms.dto.warehouse.WorkTripCostByPositionDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactWorkTripDao {
    private final JdbcTemplate jdbcTemplate;

    public FactWorkTripDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 4.a Top nhân viên có nhiều chuyến công tác nhất
    public List<WorkTripStatsByEmployeeDTO> getTopEmployeesByWorkTrips(int limit) {
        String sql = """
            SELECT TOP ? 
                employee_name,
                department_name,
                position_name,
                COUNT(*) AS total_trips,
                SUM(trip_duration) AS total_days_on_trip,
                SUM(total_cost) AS total_trip_cost
            FROM fact_work_trips
            WHERE status = 'Hoàn thành'
            GROUP BY employee_name, department_name, position_name
            ORDER BY total_trips DESC, total_days_on_trip DESC
        """;
        return jdbcTemplate.query(sql, new Object[]{limit}, new RowMapper<WorkTripStatsByEmployeeDTO>() {
            @Override
            public WorkTripStatsByEmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkTripStatsByEmployeeDTO dto = new WorkTripStatsByEmployeeDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setTotalTrips(rs.getInt("total_trips"));
                dto.setTotalDaysOnTrip(rs.getInt("total_days_on_trip"));
                dto.setTotalTripCost(rs.getBigDecimal("total_trip_cost"));
                return dto;
            }
        });
    }

    // 4.b Top điểm đến công tác phổ biến
    public List<WorkTripStatsByDestinationDTO> getTopDestinations(int limit) {
        String sql = """
            SELECT TOP ? 
                destination,
                COUNT(*) AS trip_count,
                COUNT(DISTINCT employee_name) AS distinct_employees,
                SUM(total_cost) AS total_cost,
                AVG(total_cost) AS avg_cost_per_trip,
                AVG(trip_duration) AS avg_duration
            FROM fact_work_trips
            WHERE status = 'Hoàn thành'
            GROUP BY destination
            ORDER BY trip_count DESC
        """;
        return jdbcTemplate.query(sql, new Object[]{limit}, new RowMapper<WorkTripStatsByDestinationDTO>() {
            @Override
            public WorkTripStatsByDestinationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkTripStatsByDestinationDTO dto = new WorkTripStatsByDestinationDTO();
                dto.setDestination(rs.getString("destination"));
                dto.setTripCount(rs.getInt("trip_count"));
                dto.setDistinctEmployees(rs.getInt("distinct_employees"));
                dto.setTotalCost(rs.getBigDecimal("total_cost"));
                dto.setAvgCostPerTrip(rs.getBigDecimal("avg_cost_per_trip"));
                dto.setAvgDuration(rs.getDouble("avg_duration"));
                return dto;
            }
        });
    }

    // 4.c Tổng chi phí công tác theo phòng ban
    public List<WorkTripCostByDepartmentDTO> getWorkTripCostByDepartment() {
        String sql = """
            SELECT 
                department_name,
                COUNT(*) AS total_trips,
                SUM(trip_duration) AS total_days,
                SUM(total_cost) AS total_cost,
                SUM(total_cost) / SUM(trip_duration) AS cost_per_day,
                SUM(total_cost) / COUNT(DISTINCT employee_name) AS cost_per_employee
            FROM fact_work_trips
            WHERE status = 'Hoàn thành'
            GROUP BY department_name
            ORDER BY total_cost DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<WorkTripCostByDepartmentDTO>() {
            @Override
            public WorkTripCostByDepartmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkTripCostByDepartmentDTO dto = new WorkTripCostByDepartmentDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setTotalTrips(rs.getInt("total_trips"));
                dto.setTotalDays(rs.getInt("total_days"));
                dto.setTotalCost(rs.getBigDecimal("total_cost"));
                dto.setCostPerDay(rs.getBigDecimal("cost_per_day"));
                dto.setCostPerEmployee(rs.getBigDecimal("cost_per_employee"));
                return dto;
            }
        });
    }

    // 4.d Phân tích theo mục đích công tác
    public List<WorkTripPurposeStatsDTO> getWorkTripPurposeStats() {
        String sql = """
            SELECT 
                purpose,
                COUNT(*) AS trip_count,
                COUNT(DISTINCT destination) AS distinct_destinations,
                AVG(trip_duration) AS avg_duration,
                SUM(total_cost) AS total_cost,
                SUM(total_cost) / COUNT(*) AS avg_cost_per_trip
            FROM fact_work_trips
            WHERE status = 'Hoàn thành'
            GROUP BY purpose
            ORDER BY total_cost DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<WorkTripPurposeStatsDTO>() {
            @Override
            public WorkTripPurposeStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkTripPurposeStatsDTO dto = new WorkTripPurposeStatsDTO();
                dto.setPurpose(rs.getString("purpose"));
                dto.setTripCount(rs.getInt("trip_count"));
                dto.setDistinctDestinations(rs.getInt("distinct_destinations"));
                dto.setAvgDuration(rs.getDouble("avg_duration"));
                dto.setTotalCost(rs.getBigDecimal("total_cost"));
                dto.setAvgCostPerTrip(rs.getBigDecimal("avg_cost_per_trip"));
                return dto;
            }
        });
    }

    // 4.e Xu hướng công tác theo tháng
    public List<WorkTripTrendByMonthDTO> getWorkTripTrendByMonth() {
        String sql = """
            SELECT 
                dd.year,
                dd.month,
                COUNT(*) AS trip_count,
                SUM(fwt.total_cost) AS monthly_cost,
                SUM(fwt.trip_duration) AS total_days
            FROM fact_work_trips fwt
            JOIN dim_date dd ON fwt.start_date_sk = dd.date_sk
            WHERE fwt.status = 'Hoàn thành'
            GROUP BY dd.year, dd.month
            ORDER BY dd.year, dd.month
        """;
        return jdbcTemplate.query(sql, new RowMapper<WorkTripTrendByMonthDTO>() {
            @Override
            public WorkTripTrendByMonthDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkTripTrendByMonthDTO dto = new WorkTripTrendByMonthDTO();
                dto.setYear(rs.getInt("year"));
                dto.setMonth(rs.getInt("month"));
                dto.setTripCount(rs.getInt("trip_count"));
                dto.setMonthlyCost(rs.getBigDecimal("monthly_cost"));
                dto.setTotalDays(rs.getInt("total_days"));
                return dto;
            }
        });
    }

    // 4.g Chi phí công tác theo vị trí/chức vụ
    public List<WorkTripCostByPositionDTO> getWorkTripCostByPosition() {
        String sql = """
            SELECT 
                position_name,
                COUNT(*) AS trip_count,
                SUM(total_cost) AS total_cost,
                AVG(total_cost) AS avg_cost_per_trip,
                AVG(trip_duration) AS avg_duration
            FROM fact_work_trips
            WHERE status = 'Hoàn thành'
            GROUP BY position_name
            ORDER BY total_cost DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<WorkTripCostByPositionDTO>() {
            @Override
            public WorkTripCostByPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                WorkTripCostByPositionDTO dto = new WorkTripCostByPositionDTO();
                dto.setPositionName(rs.getString("position_name"));
                dto.setTripCount(rs.getInt("trip_count"));
                dto.setTotalCost(rs.getBigDecimal("total_cost"));
                dto.setAvgCostPerTrip(rs.getBigDecimal("avg_cost_per_trip"));
                dto.setAvgDuration(rs.getDouble("avg_duration"));
                return dto;
            }
        });
    }
}

