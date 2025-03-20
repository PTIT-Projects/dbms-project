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

    public void createDependentDeduction(DependentDeduction dependentDeduction) {
        String sql = "INSERT INTO DependentsDeductions (EmployeeID, NumberOfDependents, DeductionAmount) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                dependentDeduction.getEmployee() != null ? dependentDeduction.getEmployee().getId() : null,
                dependentDeduction.getNumberOfDependents(),
                dependentDeduction.getDeductionAmount());
    }

    public DependentDeduction getDependentDeductionById(Integer id) {
        String sql = "SELECT * FROM DependentsDeductions WHERE DeductionID = ?";
        return jdbcTemplate.queryForObject(sql, dependentDeductionRowMapper, id);
    }

    public List<DependentDeduction> getAllDependentDeductions() {
        String sql = "SELECT * FROM DependentsDeductions";
        return jdbcTemplate.query(sql, dependentDeductionRowMapper);
    }

    public void updateDependentDeduction(DependentDeduction dependentDeduction) {
        String sql = "UPDATE DependentsDeductions SET EmployeeID = ?, NumberOfDependents = ?, DeductionAmount = ? WHERE DeductionID = ?";
        jdbcTemplate.update(sql,
                dependentDeduction.getEmployee() != null ? dependentDeduction.getEmployee().getId() : null,
                dependentDeduction.getNumberOfDependents(),
                dependentDeduction.getDeductionAmount(),
                dependentDeduction.getId());
    }

    public void deleteDependentDeduction(Integer id) {
        String sql = "DELETE FROM DependentsDeductions WHERE DeductionID = ?";
        jdbcTemplate.update(sql, id);
    }
}