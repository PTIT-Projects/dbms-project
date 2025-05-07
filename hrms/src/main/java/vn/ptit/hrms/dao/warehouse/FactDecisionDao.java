package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.dto.warehouse.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactDecisionDao {
    private final JdbcTemplate jdbcTemplate;

    public FactDecisionDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 7.a Số lượng quyết định theo loại và phòng ban
    public List<DecisionByDepartmentTypeDTO> getDecisionsByDepartmentAndType() {
        String sql = """
            SELECT 
                d.department_name,
                fd.decision_type,
                COUNT(fd.decision_sk) AS total_decisions
            FROM 
                fact_decision fd
            JOIN 
                dim_departments d ON fd.department_name = d.department_name
            GROUP BY 
                d.department_name, 
                fd.decision_type
            ORDER BY 
                total_decisions DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<DecisionByDepartmentTypeDTO>() {
            @Override
            public DecisionByDepartmentTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DecisionByDepartmentTypeDTO(
                    rs.getString("department_name"),
                    rs.getString("decision_type"),
                    rs.getInt("total_decisions")
                );
            }
        });
    }
    
    // 7.b Phân tích số lượng quyết định theo tháng/năm
    public List<DecisionTrendDTO> getDecisionTrendsByTime() {
        String sql = """
            SELECT 
                dd.year,
                dd.month,
                fd.decision_type,
                COUNT(fd.decision_sk) AS total_decisions
            FROM 
                fact_decision fd
            JOIN 
                dim_date dd ON fd.decision_date_sk = dd.date_sk
            GROUP BY 
                dd.year, 
                dd.month, 
                fd.decision_type
            ORDER BY 
                dd.year, 
                dd.month
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<DecisionTrendDTO>() {
            @Override
            public DecisionTrendDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DecisionTrendDTO(
                    rs.getInt("year"),
                    rs.getInt("month"),
                    rs.getString("decision_type"),
                    rs.getInt("total_decisions")
                );
            }
        });
    }
    
    // 7.c Tỷ lệ khen thưởng vs kỷ luật
    public List<DecisionTypeRatioDTO> getDecisionTypeRatios() {
        String sql = """
            SELECT 
                decision_type,
                COUNT(*) AS total_decisions,
                ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM fact_decision), 2) AS percentage
            FROM 
                fact_decision
            GROUP BY 
                decision_type
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<DecisionTypeRatioDTO>() {
            @Override
            public DecisionTypeRatioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DecisionTypeRatioDTO(
                    rs.getString("decision_type"),
                    rs.getInt("total_decisions"),
                    rs.getDouble("percentage")
                );
            }
        });
    }
    
    // 7.d Ảnh hưởng đến hành vi làm việc
    public List<EmployeeBehaviorAfterDecisionDTO> getEmployeeBehaviorAfterDisciplinary() {
        String sql = """
            SELECT 
                e.employee_sk,
                e.full_name,
                COUNT(CASE WHEN fd.decision_type = 'Kỷ luật' THEN 1 END) AS disciplinary_count,
                AVG(CAST(fa.is_late AS FLOAT)) AS avg_late_rate_after_decision
            FROM 
                fact_decision fd
            JOIN 
                dim_employees e ON fd.employee_sk = e.employee_sk
            JOIN 
                fact_attendance fa ON e.employee_sk = fa.employee_sk
                AND fa.date_sk > fd.decision_date_sk
            WHERE 
                fd.decision_type = 'Kỷ luật'
            GROUP BY 
                e.employee_sk, 
                e.full_name
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<EmployeeBehaviorAfterDecisionDTO>() {
            @Override
            public EmployeeBehaviorAfterDecisionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EmployeeBehaviorAfterDecisionDTO(
                    rs.getInt("employee_sk"),
                    rs.getString("full_name"),
                    rs.getInt("disciplinary_count"),
                    rs.getDouble("avg_late_rate_after_decision")
                );
            }
        });
    }
    
    // 7.e thời gian hiệu lực trung bình của quyết định
    public List<DecisionDurationDTO> getAverageDecisionDurations() {
        String sql = """
            SELECT 
                decision_type,
                AVG(DATEDIFF(DAY, decision_effective_date, decision_expiry_date)) AS avg_duration_days
            FROM 
                fact_decision
            WHERE 
                decision_expiry_date IS NOT NULL
            GROUP BY 
                decision_type
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<DecisionDurationDTO>() {
            @Override
            public DecisionDurationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DecisionDurationDTO(
                    rs.getString("decision_type"),
                    rs.getDouble("avg_duration_days")
                );
            }
        });
    }
    
    // 7.f Top 5 nhân viên được khen thưởng nhiều nhất
    public List<TopRewardedEmployeeDTO> getTopRewardedEmployees(int limit) {
        String sql = String.format("""
            SELECT TOP %d
                e.full_name,
                d.department_name,
                COUNT(fd.decision_sk) AS reward_count
            FROM 
                fact_decision fd
            JOIN 
                dim_employees e ON fd.employee_sk = e.employee_sk
            JOIN 
                dim_departments d ON e.department_sk = d.department_sk
            WHERE 
                fd.decision_type = 'Khen thưởng'
            GROUP BY 
                e.full_name, 
                d.department_name
            ORDER BY 
                reward_count DESC
        """, limit);
        
        return jdbcTemplate.query(sql, new RowMapper<TopRewardedEmployeeDTO>() {
            @Override
            public TopRewardedEmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TopRewardedEmployeeDTO(
                    rs.getString("full_name"),
                    rs.getString("department_name"),
                    rs.getInt("reward_count")
                );
            }
        });
    }
    
    // 7.g tương quan giữa khen thưởng và giờ làm thêm
    public List<RewardOvertimeCorrelationDTO> getRewardOvertimeCorrelation() {
        String sql = """
            SELECT 
                e.employee_sk,
                e.full_name,
                COUNT(fd.decision_sk) AS reward_count,
                SUM(fr.registration_duration) AS total_overtime_hours
            FROM 
                fact_decision fd
            JOIN 
                dim_employees e ON fd.employee_sk = e.employee_sk
            LEFT JOIN 
                fact_registrations fr ON e.employee_sk = fr.employee_sk
                AND fr.registration_type = 'Làm thêm'
            WHERE 
                fd.decision_type = 'Khen thưởng'
            GROUP BY 
                e.employee_sk, 
                e.full_name
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RewardOvertimeCorrelationDTO>() {
            @Override
            public RewardOvertimeCorrelationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RewardOvertimeCorrelationDTO(
                    rs.getInt("employee_sk"),
                    rs.getString("full_name"),
                    rs.getInt("reward_count"),
                    rs.getDouble("total_overtime_hours")
                );
            }
        });
    }
    
    // 7.h phân tích theo chức vụ
    public List<DecisionByPositionDTO> getDecisionsByPosition() {
        String sql = """
            SELECT 
                p.position_name,
                fd.decision_type,
                COUNT(*) AS decision_count
            FROM 
                fact_decision fd
            JOIN 
                dim_employees e ON fd.employee_sk = e.employee_sk
            JOIN 
                dim_positions p ON e.position_sk = p.position_sk
            GROUP BY 
                p.position_name, 
                fd.decision_type
            ORDER BY 
                decision_count DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<DecisionByPositionDTO>() {
            @Override
            public DecisionByPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DecisionByPositionDTO(
                    rs.getString("position_name"),
                    rs.getString("decision_type"),
                    rs.getInt("decision_count")
                );
            }
        });
    }
}
