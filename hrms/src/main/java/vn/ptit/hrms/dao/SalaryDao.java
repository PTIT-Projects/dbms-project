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

   
    public void createSalary(Salary salary) {
        String sql = "INSERT INTO Salary (EmployeeID, Month, Year, BasicSalary, Allowance, Deductions, NetSalary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                salary.getEmployee() != null ? salary.getEmployee().getId() : null,
                salary.getMonth(),
                salary.getYear(),
                salary.getBasicSalary(),
                salary.getAllowance(),
                salary.getDeductions(),
                salary.getBasicSalary() + salary.getAllowance() - salary.getDeductions());
    }

   
    public Salary getSalaryById(Integer id) {
        String sql = "SELECT * FROM Salary WHERE SalaryID = ?";
        return jdbcTemplate.queryForObject(sql, salaryRowMapper, id);
    }

   
    public List<Salary> getAllSalaries() {
        String sql = "SELECT * FROM Salary";
        return jdbcTemplate.query(sql, salaryRowMapper);
    }

   
    public void updateSalary(Salary salary) {
        if (salary.getEmployee() == null || salary.getEmployee().getId() == null) {
            Salary existingSalary = getSalaryById(salary.getId());
            if (existingSalary != null && existingSalary.getEmployee() != null) {
                salary.setEmployee(existingSalary.getEmployee());
            }
        }

        String sql = "UPDATE Salary SET EmployeeID = ?, Month = ?, Year = ?, BasicSalary = ?, Allowance = ?, Deductions = ?, NetSalary = ? WHERE SalaryID = ?";
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

   
    public void deleteSalary(Integer id) {
        String sql = "DELETE FROM Salary WHERE SalaryID = ?";
        jdbcTemplate.update(sql, id);
    }
}