package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Insurance;
import vn.ptit.hrms.mapper.InsuranceRowMapper;

import java.util.List;

@Repository
public class InsuranceDao {
    private final JdbcTemplate jdbcTemplate;
    private final InsuranceRowMapper insuranceRowMapper;

    public InsuranceDao(JdbcTemplate jdbcTemplate, InsuranceRowMapper insuranceRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.insuranceRowMapper = insuranceRowMapper;
    }

    // Method to create a new insurance record
    public void createInsurance(Insurance insurance) {
        String sql = "INSERT INTO Insurance (EmployeeID, InsuranceNumber, InsuranceType, StartDate, EndDate) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                insurance.getEmployee() != null ? insurance.getEmployee().getId() : null,
                insurance.getInsuranceNumber(),
                insurance.getInsuranceType(),
                insurance.getStartDate(),
                insurance.getEndDate());
    }

    // Method to get an insurance record by ID
    public Insurance getInsuranceById(Integer id) {
        String sql = "SELECT * FROM Insurance WHERE InsuranceID = ?";
        return jdbcTemplate.queryForObject(sql, insuranceRowMapper, id);
    }

    // Method to get all insurance records
    public List<Insurance> getAllInsurances() {
        String sql = "SELECT * FROM Insurance";
        return jdbcTemplate.query(sql, insuranceRowMapper);
    }

    // Method to update an insurance record
    public void updateInsurance(Insurance insurance) {
        String sql = "UPDATE Insurance SET EmployeeID = ?, InsuranceNumber = ?, InsuranceType = ?, StartDate = ?, EndDate = ? WHERE InsuranceID = ?";
        jdbcTemplate.update(sql,
                insurance.getEmployee() != null ? insurance.getEmployee().getId() : null,
                insurance.getInsuranceNumber(),
                insurance.getInsuranceType(),
                insurance.getStartDate(),
                insurance.getEndDate(),
                insurance.getId());
    }

    // Method to delete an insurance record
    public void deleteInsurance(Integer id) {
        String sql = "DELETE FROM Insurance WHERE InsuranceID = ?";
        jdbcTemplate.update(sql, id);
    }
}