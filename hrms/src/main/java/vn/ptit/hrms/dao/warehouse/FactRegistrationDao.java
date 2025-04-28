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
public class FactRegistrationDao {
    private final JdbcTemplate jdbcTemplate;

    public FactRegistrationDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 6.a Số lượng đăng ký theo loại và phòng ban
    public List<RegistrationByDepartmentTypeDTO> getRegistrationsByDepartmentAndType() {
        String sql = """
            SELECT 
                r.department_name,
                r.registration_type,
                COUNT(r.registration_sk) AS total_registrations
            FROM 
                fact_registrations r
            GROUP BY 
                r.department_name, 
                r.registration_type
            ORDER BY 
                r.department_name, 
                total_registrations DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RegistrationByDepartmentTypeDTO>() {
            @Override
            public RegistrationByDepartmentTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RegistrationByDepartmentTypeDTO(
                    rs.getString("department_name"),
                    rs.getString("registration_type"),
                    rs.getInt("total_registrations")
                );
            }
        });
    }
    
    // 6.b Số lượng đăng ký theo loại của từng nhân viên
    public List<RegistrationByEmployeeTypeDTO> getRegistrationsByEmployeeAndType() {
        String sql = """
            SELECT 
                e.full_name,
                r.registration_type,
                COUNT(r.registration_sk) AS total_registrations
            FROM 
                fact_registrations r
            JOIN 
                dim_employees e ON r.employee_sk = e.employee_sk
            GROUP BY 
                e.full_name, 
                r.registration_type
            ORDER BY 
                e.full_name, 
                total_registrations DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RegistrationByEmployeeTypeDTO>() {
            @Override
            public RegistrationByEmployeeTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RegistrationByEmployeeTypeDTO(
                    rs.getString("full_name"),
                    rs.getString("registration_type"),
                    rs.getInt("total_registrations")
                );
            }
        });
    }
    
    // 6.c Tỷ lệ duyệt đăng ký theo loại
    public List<RegistrationApprovalRateDTO> getRegistrationApprovalRatesByType() {
        String sql = """
            SELECT 
                registration_type,
                COUNT(*) AS total_registrations,
                SUM(CASE WHEN status = 'Đã duyệt' THEN 1 ELSE 0 END) AS approved_count,
                ROUND(
                    SUM(CASE WHEN status = 'Đã duyệt' THEN 1.0 ELSE 0 END) / COUNT(*) * 100, 
                    2
                ) AS approval_rate_percentage
            FROM 
                fact_registrations
            GROUP BY 
                registration_type
            ORDER BY 
                approval_rate_percentage DESC
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RegistrationApprovalRateDTO>() {
            @Override
            public RegistrationApprovalRateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RegistrationApprovalRateDTO(
                    rs.getString("registration_type"),
                    rs.getInt("total_registrations"),
                    rs.getInt("approved_count"),
                    rs.getDouble("approval_rate_percentage")
                );
            }
        });
    }
    
    // 6.d Xu hướng đăng ký theo thời gian
    public List<RegistrationTrendDTO> getRegistrationTrendsByTime() {
        String sql = """
            SELECT 
                d.year,
                d.month,
                r.registration_type,
                COUNT(r.registration_sk) AS total_registrations
            FROM 
                fact_registrations r
            JOIN 
                dim_date d ON r.request_date_sk = d.date_sk
            GROUP BY 
                d.year, 
                d.month, 
                r.registration_type
            ORDER BY 
                d.year, 
                d.month
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RegistrationTrendDTO>() {
            @Override
            public RegistrationTrendDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RegistrationTrendDTO(
                    rs.getInt("year"),
                    rs.getInt("month"),
                    rs.getString("registration_type"),
                    rs.getInt("total_registrations")
                );
            }
        });
    }
    
    // 6.e top 5 phòng ban có số đăng ký cao nhất 
    public List<RegistrationByDepartmentTypeDTO> getTopDepartmentsByRegistrationCount(int limit) {
        String sql = String.format("""
            SELECT TOP %d
                department_name,
                'All Types' as registration_type,
                COUNT(registration_sk) AS total_registrations
            FROM 
                fact_registrations
            GROUP BY 
                department_name
            ORDER BY 
                total_registrations DESC
        """, limit);
        
        return jdbcTemplate.query(sql, new RowMapper<RegistrationByDepartmentTypeDTO>() {
            @Override
            public RegistrationByDepartmentTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RegistrationByDepartmentTypeDTO(
                    rs.getString("department_name"),
                    rs.getString("registration_type"),
                    rs.getInt("total_registrations")
                );
            }
        });
    }
    
    // 6.f thống kê đăng ký theo trạng thái
    public List<RegistrationByStatusDTO> getRegistrationsByStatusAndType() {
        String sql = """
            SELECT 
                status,
                registration_type,
                COUNT(*) AS count,
                ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (PARTITION BY registration_type), 2) AS percentage
            FROM 
                fact_registrations
            GROUP BY 
                registration_type, 
                status
        """;
        
        return jdbcTemplate.query(sql, new RowMapper<RegistrationByStatusDTO>() {
            @Override
            public RegistrationByStatusDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new RegistrationByStatusDTO(
                    rs.getString("status"),
                    rs.getString("registration_type"),
                    rs.getInt("count"),
                    rs.getDouble("percentage")
                );
            }
        });
    }
    
    // 6.g số gio làm thêm trung bình được duyệt
    public double getAverageApprovedOvertimeHours() {
        String sql = """
            SELECT 
                AVG(registration_duration) AS avg_overtime_hours
            FROM 
                fact_registrations
            WHERE 
                registration_type = 'Làm thêm' 
                AND status = 'Đã duyệt'
        """;
        
        return jdbcTemplate.queryForObject(sql, Double.class);
    }
}
