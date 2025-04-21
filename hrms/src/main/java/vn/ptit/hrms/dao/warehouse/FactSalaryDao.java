package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.FactSalary;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class FactSalaryDao {
    private final JdbcTemplate jdbcTemplate;

    public FactSalaryDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FactSalary> getSalaryByEmployeeSk(Integer employeeSk) {
        String sql = "SELECT * FROM fact_salary WHERE employee_sk = ?";
        return jdbcTemplate.query(sql, new FactSalaryRowMapper(), employeeSk);
    }

    public FactSalary getSalaryByEmployeeAndMonth(Integer employeeSk, Integer dateSk) {
        String sql = "SELECT * FROM fact_salary WHERE employee_sk = ? AND date_sk = ?";
        return jdbcTemplate.queryForObject(sql, new FactSalaryRowMapper(), employeeSk, dateSk);
    }

    public List<FactSalary> getSalaryByMonth(Integer dateSk) {
        String sql = "SELECT * FROM fact_salary WHERE date_sk = ?";
        return jdbcTemplate.query(sql, new FactSalaryRowMapper(), dateSk);
    }

    public List<Map<String, Object>> getAverageSalaryByDepartment() {
        String sql = "SELECT department_name, AVG(total_salary) as avg_salary FROM fact_salary GROUP BY department_name";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getSalaryDistributionByPosition() {
        String sql = "SELECT position_name, AVG(basic_salary) as avg_basic, AVG(allowance) as avg_allowance, AVG(deductions) as avg_deductions FROM fact_salary GROUP BY position_name";
        return jdbcTemplate.queryForList(sql);
    }

    public BigDecimal getTotalPayrollByMonth(Integer dateSk) {
        String sql = "SELECT SUM(total_salary) FROM fact_salary WHERE date_sk = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, dateSk);
    }

    private static class FactSalaryRowMapper implements RowMapper<FactSalary> {
        @Override
        public FactSalary mapRow(ResultSet rs, int rowNum) throws SQLException {
            FactSalary salary = new FactSalary();
            salary.setSalarySk(rs.getInt("salary_sk"));
            salary.setSalaryId(rs.getInt("salary_id"));
            salary.setEmployeeSk(rs.getInt("employee_sk"));
            salary.setEmployeeName(rs.getString("employee_name"));
            salary.setDepartmentName(rs.getString("department_name"));
            salary.setPositionName(rs.getString("position_name"));
            salary.setDateSk(rs.getInt("date_sk"));
            salary.setBasicSalary(rs.getBigDecimal("basic_salary"));
            salary.setAllowance(rs.getBigDecimal("allowance"));
            salary.setDeductions(rs.getBigDecimal("deductions"));
            salary.setTotalSalary(rs.getBigDecimal("total_salary"));
            salary.setPaymentStatus(rs.getString("payment_status"));
            return salary;
        }
    }
}