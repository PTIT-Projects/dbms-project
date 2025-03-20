package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Salary;
import vn.ptit.hrms.mapper.SalaryRowMapper;

import java.util.List;

@Repository
public class SalaryDao {
    private final JdbcTemplate jdbcTemplate;
    private final SalaryRowMapper salaryRowMapper;

    public SalaryDao(JdbcTemplate jdbcTemplate, SalaryRowMapper salaryRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.salaryRowMapper = salaryRowMapper;
    }

    // Method to create a new salary record
    public void createSalary(Salary salary) {
        String sql = "INSERT INTO salaries (employee_id, month, year, basic_salary, allowance, deductions, net_salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                salary.getEmployee() != null ? salary.getEmployee().getId() : null,
                salary.getMonth(),
                salary.getYear(),
                salary.getBasicSalary(),
                salary.getAllowance(),
                salary.getDeductions(),
                salary.getNetSalary());
    }

    // Method to get a salary record by ID
    public Salary getSalaryById(Integer id) {
        String sql = "SELECT * FROM salaries WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, salaryRowMapper, id);
    }

    // Method to get all salary records
    public List<Salary> getAllSalaries() {
        String sql = "SELECT * FROM salaries";
        return jdbcTemplate.query(sql, salaryRowMapper);
    }

    // Method to update a salary record
    public void updateSalary(Salary salary) {
        String sql = "UPDATE salaries SET employee_id = ?, month = ?, year = ?, basic_salary = ?, allowance = ?, deductions = ?, net_salary = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                salary.getEmployee() != null ? salary.getEmployee().getId() : null,
                salary.getMonth(),
                salary.getYear(),
                salary.getBasicSalary(),
                salary.getAllowance(),
                salary.getDeductions(),
                salary.getNetSalary(),
                salary.getId());
    }

    // Method to delete a salary record
    public void deleteSalary(Integer id) {
        String sql = "DELETE FROM salaries WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
