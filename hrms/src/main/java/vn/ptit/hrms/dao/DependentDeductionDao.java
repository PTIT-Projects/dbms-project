package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.DependentDeduction;
import vn.ptit.hrms.mapper.DependentDeductionRowMapper;

import java.util.List;

@Repository
public class DependentDeductionDao {
    private final JdbcTemplate jdbcTemplate;
    private final DependentDeductionRowMapper dependentDeductionRowMapper;

    public DependentDeductionDao(JdbcTemplate jdbcTemplate, DependentDeductionRowMapper dependentDeductionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dependentDeductionRowMapper = dependentDeductionRowMapper;
    }

    // Method to create a new dependent deduction
    public void createDependentDeduction(DependentDeduction dependentDeduction) {
        String sql = "INSERT INTO DependentsDeductions (EmployeeID, NumberOfDependents, DeductionAmount) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                dependentDeduction.getEmployee() != null ? dependentDeduction.getEmployee().getId() : null,
                dependentDeduction.getNumberOfDependents(),
                dependentDeduction.getDeductionAmount());
    }

    // Method to get a dependent deduction by ID
    public DependentDeduction getDependentDeductionById(Integer id) {
        String sql = "SELECT * FROM DependentsDeductions WHERE DeductionID = ?";
        return jdbcTemplate.queryForObject(sql, dependentDeductionRowMapper, id);
    }

    // Method to get all dependent deductions
    public List<DependentDeduction> getAllDependentDeductions() {
        String sql = "SELECT * FROM DependentsDeductions";
        return jdbcTemplate.query(sql, dependentDeductionRowMapper);
    }

    // Method to update a dependent deduction
    public void updateDependentDeduction(DependentDeduction dependentDeduction) {
        String sql = "UPDATE DependentsDeductions SET EmployeeID = ?, NumberOfDependents = ?, DeductionAmount = ? WHERE DeductionID = ?";
        jdbcTemplate.update(sql,
                dependentDeduction.getEmployee() != null ? dependentDeduction.getEmployee().getId() : null,
                dependentDeduction.getNumberOfDependents(),
                dependentDeduction.getDeductionAmount(),
                dependentDeduction.getId());
    }

    // Method to delete a dependent deduction
    public void deleteDependentDeduction(Integer id) {
        String sql = "DELETE FROM DependentsDeductions WHERE DeductionID = ?";
        jdbcTemplate.update(sql, id);
    }
}