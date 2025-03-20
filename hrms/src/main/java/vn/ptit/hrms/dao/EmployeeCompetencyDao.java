package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.EmployeeCompetency;
import vn.ptit.hrms.mapper.EmployeeCompetencyRowMapper;

import java.util.List;

@Repository
public class EmployeeCompetencyDao {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeCompetencyRowMapper employeeCompetencyRowMapper;

    public EmployeeCompetencyDao(JdbcTemplate jdbcTemplate, EmployeeCompetencyRowMapper employeeCompetencyRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeeCompetencyRowMapper = employeeCompetencyRowMapper;
    }

    // Method to create a new employee competency
    public void createEmployeeCompetency(EmployeeCompetency employeeCompetency) {
        String sql = "INSERT INTO EmployeeCompetencies (EmployeeID, CompetencyID, Level) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                employeeCompetency.getEmployee() != null ? employeeCompetency.getEmployee().getId() : null,
                employeeCompetency.getCompetency() != null ? employeeCompetency.getCompetency().getId() : null,
                employeeCompetency.getLevel() != null ? employeeCompetency.getLevel().getValue() : null);
    }

    // Method to get an employee competency by ID
    public EmployeeCompetency getEmployeeCompetencyById(Integer id) {
        String sql = "SELECT * FROM EmployeeCompetencies WHERE EmployeeCompetencyID = ?";
        return jdbcTemplate.queryForObject(sql, employeeCompetencyRowMapper, id);
    }

    // Method to get all employee competencies
    public List<EmployeeCompetency> getAllEmployeeCompetencies() {
        String sql = "SELECT * FROM EmployeeCompetencies";
        return jdbcTemplate.query(sql, employeeCompetencyRowMapper);
    }

    // Method to update an employee competency
    public void updateEmployeeCompetency(EmployeeCompetency employeeCompetency) {
        String sql = "UPDATE EmployeeCompetencies SET EmployeeID = ?, CompetencyID = ?, Level = ? WHERE EmployeeCompetencyID = ?";
        jdbcTemplate.update(sql,
                employeeCompetency.getEmployee() != null ? employeeCompetency.getEmployee().getId() : null,
                employeeCompetency.getCompetency() != null ? employeeCompetency.getCompetency().getId() : null,
                employeeCompetency.getLevel() != null ? employeeCompetency.getLevel().getValue() : null,
                employeeCompetency.getId());
    }

    // Method to delete an employee competency
    public void deleteEmployeeCompetency(Integer id) {
        String sql = "DELETE FROM EmployeeCompetencies WHERE EmployeeCompetencyID = ?";
        jdbcTemplate.update(sql, id);
    }
}