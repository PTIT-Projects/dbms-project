package vn.ptit.hrms.dao.warehouse;

import vn.ptit.hrms.dto.warehouse.SalaryStatsByDeptPositionDTO;
import vn.ptit.hrms.dto.warehouse.TopSalaryEmployeeDTO;
import vn.ptit.hrms.dto.warehouse.SalaryRatioByDepartmentDTO;
import vn.ptit.hrms.dto.warehouse.SalaryRatioByEmployeeDTO;
import vn.ptit.hrms.dto.warehouse.SalaryTrendByMonthDTO;
import vn.ptit.hrms.dto.warehouse.SalaryByAgeGenderDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactSalaryDao {
    private final JdbcTemplate jdbcTemplate;

    public FactSalaryDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 2.a Lương trung bình theo phòng ban và vị trí
    public List<SalaryStatsByDeptPositionDTO> getSalaryStatsByDeptPosition() {
        String sql = """
            SELECT 
                department_name,
                position_name,
                COUNT(DISTINCT employee_name) AS employee_count,
                AVG(basic_salary) AS avg_basic_salary,
                AVG(allowance) AS avg_allowance,
                AVG(deductions) AS avg_deductions,
                AVG(total_salary) AS avg_total_salary
            FROM fact_salary
            WHERE payment_status = 'Đã thanh toán'
            GROUP BY department_name, position_name
            ORDER BY department_name, avg_total_salary DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<SalaryStatsByDeptPositionDTO>() {
            @Override
            public SalaryStatsByDeptPositionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalaryStatsByDeptPositionDTO dto = new SalaryStatsByDeptPositionDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setEmployeeCount(rs.getInt("employee_count"));
                dto.setAvgBasicSalary(rs.getBigDecimal("avg_basic_salary"));
                dto.setAvgAllowance(rs.getBigDecimal("avg_allowance"));
                dto.setAvgDeductions(rs.getBigDecimal("avg_deductions"));
                dto.setAvgTotalSalary(rs.getBigDecimal("avg_total_salary"));
                return dto;
            }
        });
    }

    // 2.b Top nhân viên có tổng lương cao nhất
    public List<TopSalaryEmployeeDTO> getTopEmployeesByHighestSalary(int limit) {
        String sql = String.format("""
            SELECT TOP %d 
                employee_name,
                department_name,
                position_name,
                total_salary,
                basic_salary,
                allowance,
                deductions,
                ROUND((total_salary * 100.0) / (SELECT MAX(total_salary) FROM fact_salary), 2) AS salary_percentage
            FROM fact_salary
            WHERE payment_status = 'Đã thanh toán'
            ORDER BY total_salary DESC
        """, limit);
        return jdbcTemplate.query(sql, new RowMapper<TopSalaryEmployeeDTO>() {
            @Override
            public TopSalaryEmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                TopSalaryEmployeeDTO dto = new TopSalaryEmployeeDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setTotalSalary(rs.getBigDecimal("total_salary"));
                dto.setBasicSalary(rs.getBigDecimal("basic_salary"));
                dto.setAllowance(rs.getBigDecimal("allowance"));
                dto.setDeductions(rs.getBigDecimal("deductions"));
                dto.setSalaryPercentage(rs.getDouble("salary_percentage"));
                return dto;
            }
        });
    }

    // 2.b Top nhân viên có tổng lương thấp nhất
    public List<TopSalaryEmployeeDTO> getTopEmployeesByLowestSalary(int limit) {
        String sql = String.format("""
            SELECT TOP %d 
                employee_name,
                department_name,
                position_name,
                total_salary,
                basic_salary,
                allowance,
                deductions
            FROM fact_salary
            WHERE payment_status = 'Đã thanh toán'
            ORDER BY total_salary ASC
        """, limit);
        return jdbcTemplate.query(sql, new RowMapper<TopSalaryEmployeeDTO>() {
            @Override
            public TopSalaryEmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                TopSalaryEmployeeDTO dto = new TopSalaryEmployeeDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setTotalSalary(rs.getBigDecimal("total_salary"));
                dto.setBasicSalary(rs.getBigDecimal("basic_salary"));
                dto.setAllowance(rs.getBigDecimal("allowance"));
                dto.setDeductions(rs.getBigDecimal("deductions"));
                return dto;
            }
        });
    }

    // 2.c Tỷ lệ lương cơ bản/tổng lương theo phòng ban
    public List<SalaryRatioByDepartmentDTO> getSalaryRatioByDepartment() {
        String sql = """
            SELECT 
                department_name,
                AVG(basic_salary) AS avg_basic,
                AVG(total_salary) AS avg_total,
                ROUND(AVG(basic_salary * 100.0 / total_salary), 2) AS basic_salary_ratio,
                ROUND(AVG(allowance * 100.0 / total_salary), 2) AS allowance_ratio,
                ROUND(AVG(deductions * 100.0 / total_salary), 2) AS deductions_ratio
            FROM fact_salary
            WHERE total_salary > 0 AND payment_status = 'Đã thanh toán'
            GROUP BY department_name
            ORDER BY basic_salary_ratio DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<SalaryRatioByDepartmentDTO>() {
            @Override
            public SalaryRatioByDepartmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalaryRatioByDepartmentDTO dto = new SalaryRatioByDepartmentDTO();
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setAvgBasic(rs.getBigDecimal("avg_basic"));
                dto.setAvgTotal(rs.getBigDecimal("avg_total"));
                dto.setBasicSalaryRatio(rs.getDouble("basic_salary_ratio"));
                dto.setAllowanceRatio(rs.getDouble("allowance_ratio"));
                dto.setDeductionsRatio(rs.getDouble("deductions_ratio"));
                return dto;
            }
        });
    }

    // 2.c Tỷ lệ lương cơ bản/tổng lương theo từng nhân viên
    public List<SalaryRatioByEmployeeDTO> getSalaryRatioByEmployee() {
        String sql = """
            SELECT 
                employee_name,
                department_name,
                position_name,
                basic_salary,
                allowance,
                deductions,
                total_salary,
                ROUND(basic_salary * 100.0 / total_salary, 2) AS basic_salary_percentage,
                ROUND(allowance * 100.0 / total_salary, 2) AS allowance_percentage,
                ROUND(deductions * 100.0 / total_salary, 2) AS deductions_percentage
            FROM fact_salary
            WHERE total_salary > 0 AND payment_status = 'Đã thanh toán'
            ORDER BY basic_salary_percentage DESC
        """;
        return jdbcTemplate.query(sql, new RowMapper<SalaryRatioByEmployeeDTO>() {
            @Override
            public SalaryRatioByEmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalaryRatioByEmployeeDTO dto = new SalaryRatioByEmployeeDTO();
                dto.setEmployeeName(rs.getString("employee_name"));
                dto.setDepartmentName(rs.getString("department_name"));
                dto.setPositionName(rs.getString("position_name"));
                dto.setBasicSalary(rs.getBigDecimal("basic_salary"));
                dto.setAllowance(rs.getBigDecimal("allowance"));
                dto.setDeductions(rs.getBigDecimal("deductions"));
                dto.setTotalSalary(rs.getBigDecimal("total_salary"));
                dto.setBasicSalaryPercentage(rs.getDouble("basic_salary_percentage"));
                dto.setAllowancePercentage(rs.getDouble("allowance_percentage"));
                dto.setDeductionsPercentage(rs.getDouble("deductions_percentage"));
                return dto;
            }
        });
    }

    // 2.d Xu hướng lương theo tháng năm cụ thể
    public List<SalaryTrendByMonthDTO> getSalaryTrendByMonth(int year) {
        String sql = """
            SELECT 
                dd.year,
                dd.month,
                AVG(fs.basic_salary) AS avg_basic,
                AVG(fs.total_salary) AS avg_total,
                SUM(fs.total_salary) AS department_total_payroll
            FROM fact_salary fs
            INNER JOIN dim_date dd ON fs.date_sk = dd.date_sk
            WHERE dd.year = ? AND fs.payment_status = 'Đã thanh toán'
            GROUP BY dd.year, dd.month, fs.department_name
            ORDER BY dd.year, dd.month
        """;
        return jdbcTemplate.query(sql, new Object[]{year}, new RowMapper<SalaryTrendByMonthDTO>() {
            @Override
            public SalaryTrendByMonthDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalaryTrendByMonthDTO dto = new SalaryTrendByMonthDTO();
                dto.setYear(rs.getInt("year"));
                dto.setMonth(rs.getInt("month"));
                dto.setAvgBasic(rs.getBigDecimal("avg_basic"));
                dto.setAvgTotal(rs.getBigDecimal("avg_total"));
                dto.setDepartmentTotalPayroll(rs.getBigDecimal("department_total_payroll"));
                return dto;
            }
        });
    }

    // 2.e Phân tích lương theo độ tuổi và giới tính
    public List<SalaryByAgeGenderDTO> getSalaryByAgeGender() {
        String sql = """
            SELECT 
                de.gender,
                CASE 
                    WHEN de.age < 25 THEN 'Dưới 25'
                    WHEN de.age BETWEEN 25 AND 35 THEN '25-35'
                    WHEN de.age BETWEEN 36 AND 45 THEN '36-45'
                    WHEN de.age > 45 THEN 'Trên 45'
                END AS age_group,
                AVG(fs.total_salary) AS avg_salary,
                COUNT(*) AS employee_count
            FROM fact_salary fs
            INNER JOIN dim_employees de ON fs.employee_sk = de.employee_sk
            WHERE fs.payment_status = 'Đã thanh toán'
            GROUP BY 
                de.gender,
                CASE 
                    WHEN de.age < 25 THEN 'Dưới 25'
                    WHEN de.age BETWEEN 25 AND 35 THEN '25-35'
                    WHEN de.age BETWEEN 36 AND 45 THEN '36-45'
                    WHEN de.age > 45 THEN 'Trên 45'
                END
            ORDER BY age_group, gender
        """;
        return jdbcTemplate.query(sql, new RowMapper<SalaryByAgeGenderDTO>() {
            @Override
            public SalaryByAgeGenderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalaryByAgeGenderDTO dto = new SalaryByAgeGenderDTO();
                dto.setGender(rs.getString("gender"));
                dto.setAgeGroup(rs.getString("age_group"));
                dto.setAvgSalary(rs.getBigDecimal("avg_salary"));
                dto.setEmployeeCount(rs.getInt("employee_count"));
                return dto;
            }
        });
    }
}
