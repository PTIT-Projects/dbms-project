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
        String sql = "INSERT INTO insurances (employee_id, insurance_number, insurance_type, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                insurance.getEmployee() != null ? insurance.getEmployee().getId() : null,
                insurance.getInsuranceNumber(),
                insurance.getInsuranceType(),
                insurance.getStartDate() != null ? java.sql.Date.valueOf(insurance.getStartDate()) : null,
                insurance.getEndDate() != null ? java.sql.Date.valueOf(insurance.getEndDate()) : null);
    }

    // Method to get an insurance record by ID
    public Insurance getInsuranceById(Integer id) {
        String sql = "SELECT * FROM insurances WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, insuranceRowMapper, id);
    }

    // Method to get all insurance records
    public List<Insurance> getAllInsurances() {
        String sql = "SELECT * FROM insurances";
        return jdbcTemplate.query(sql, insuranceRowMapper);
    }

    // Method to update an insurance record
    public void updateInsurance(Insurance insurance) {
        String sql = "UPDATE insurances SET employee_id = ?, insurance_number = ?, insurance_type = ?, start_date = ?, end_date = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                insurance.getEmployee() != null ? insurance.getEmployee().getId() : null,
                insurance.getInsuranceNumber(),
                insurance.getInsuranceType(),
                insurance.getStartDate() != null ? java.sql.Date.valueOf(insurance.getStartDate()) : null,
                insurance.getEndDate() != null ? java.sql.Date.valueOf(insurance.getEndDate()) : null,
                insurance.getId());
    }

    // Method to delete an insurance record
    public void deleteInsurance(Integer id) {
        String sql = "DELETE FROM insurances WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
