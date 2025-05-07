package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.dto.warehouse.RecruitmentPlanByDepartmentPositionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactRecruitmentPlanDao {
    private final JdbcTemplate jdbcTemplate;

    public FactRecruitmentPlanDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 5.a số lượng vị trí còn trống theo phòng ban / vị trí
    public List<RecruitmentPlanByDepartmentPositionDTO> getRecruitmentPlansByDepartmentPosition() {
        String sql = """
            SELECT 
                department_name,
                position_name,
                SUM(quantity) AS total_positions,
                SUM(remaining_positions) AS remaining_positions,
                SUM(quantity - remaining_positions) AS filled_positions
            FROM fact_recruitment_plan
            GROUP BY department_name, position_name
            ORDER BY remaining_positions DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RecruitmentPlanByDepartmentPositionDTO>() {
            @Override
            public RecruitmentPlanByDepartmentPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RecruitmentPlanByDepartmentPositionDTO dto = new RecruitmentPlanByDepartmentPositionDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setQuantity(rs.getInt("total_positions"));
                dto.setRemainingPositions(rs.getInt("remaining_positions"));
                dto.setFilledPositions(rs.getInt("filled_positions"));
                return dto;
            }
        });
    }
    
    // Get top departments with most open positions
    public List<RecruitmentPlanByDepartmentPositionDTO> getTopDepartmentsWithOpenPositions(int limit) {
        String sql = String.format("""
            SELECT TOP %d
                department_name,
                SUM(quantity) AS total_positions,
                SUM(remaining_positions) AS remaining_positions,
                SUM(quantity - remaining_positions) AS filled_positions
            FROM fact_recruitment_plan
            GROUP BY department_name
            ORDER BY remaining_positions DESC
        """, limit);
        
        return jdbcTemplate.query(sql, new RowMapper<RecruitmentPlanByDepartmentPositionDTO>() {
            @Override
            public RecruitmentPlanByDepartmentPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RecruitmentPlanByDepartmentPositionDTO dto = new RecruitmentPlanByDepartmentPositionDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName("All Positions"); // Indicating all positions in department
                dto.setQuantity(rs.getInt("total_positions"));
                dto.setRemainingPositions(rs.getInt("remaining_positions"));
                dto.setFilledPositions(rs.getInt("filled_positions"));
                return dto;
            }
        });
    }
    
    // Get top positions with most open positions
    public List<RecruitmentPlanByDepartmentPositionDTO> getTopPositionsWithOpenPositions(int limit) {
        String sql = String.format("""
            SELECT TOP %d
                position_name,
                SUM(quantity) AS total_positions,
                SUM(remaining_positions) AS remaining_positions,
                SUM(quantity - remaining_positions) AS filled_positions
            FROM fact_recruitment_plan
            GROUP BY position_name
            ORDER BY remaining_positions DESC
        """, limit);
        
        return jdbcTemplate.query(sql, new RowMapper<RecruitmentPlanByDepartmentPositionDTO>() {
            @Override
            public RecruitmentPlanByDepartmentPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RecruitmentPlanByDepartmentPositionDTO dto = new RecruitmentPlanByDepartmentPositionDTO();
                dto.setDepartmentName("All Departments"); // Indicating all departments
                dto.setPositionName(rs.getString("position_name"));
                dto.setQuantity(rs.getInt("total_positions"));
                dto.setRemainingPositions(rs.getInt("remaining_positions"));
                dto.setFilledPositions(rs.getInt("filled_positions"));
                return dto;
            }
        });
    }
}

