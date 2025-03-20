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
        String sql = "INSERT INTO dependent_deductions (employee_id, number_of_dependents, deduction_amount) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                dependentDeduction.getEmployee() != null ? dependentDeduction.getEmployee().getId() : null,
                dependentDeduction.getNumberOfDependents(),
                dependentDeduction.getDeductionAmount());
    }

    // Method to get a dependent deduction by ID
    public DependentDeduction getDependentDeductionById(Integer id) {
        String sql = "SELECT * FROM dependent_deductions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, dependentDeductionRowMapper, id);
    }

    // Method to get all dependent deductions
    public List<DependentDeduction> getAllDependentDeductions() {
        String sql = "SELECT * FROM dependent_deductions";
        return jdbcTemplate.query(sql, dependentDeductionRowMapper);
    }

    // Method to update a dependent deduction
    public void updateDependentDeduction(DependentDeduction dependentDeduction) {
        String sql = "UPDATE dependent_deductions SET employee_id = ?, number_of_dependents = ?, deduction_amount = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                dependentDeduction.getEmployee() != null ? dependentDeduction.getEmployee().getId() : null,
                dependentDeduction.getNumberOfDependents(),
                dependentDeduction.getDeductionAmount(),
                dependentDeduction.getId());
    }

    // Method to delete a dependent deduction
    public void deleteDependentDeduction(Integer id) {
        String sql = "DELETE FROM dependent_deductions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
