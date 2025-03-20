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
        String sql = "INSERT INTO employee_competencies (employee_id, competency_id, level) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                employeeCompetency.getEmployee() != null ? employeeCompetency.getEmployee().getId() : null,
                employeeCompetency.getCompetency() != null ? employeeCompetency.getCompetency().getId() : null,
                employeeCompetency.getLevel() != null ? employeeCompetency.getLevel().name() : null);
    }

    // Method to get an employee competency by ID
    public EmployeeCompetency getEmployeeCompetencyById(Integer id) {
        String sql = "SELECT * FROM employee_competencies WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, employeeCompetencyRowMapper, id);
    }

    // Method to get all employee competencies
    public List<EmployeeCompetency> getAllEmployeeCompetencies() {
        String sql = "SELECT * FROM employee_competencies";
        return jdbcTemplate.query(sql, employeeCompetencyRowMapper);
    }

    // Method to update an employee competency
    public void updateEmployeeCompetency(EmployeeCompetency employeeCompetency) {
        String sql = "UPDATE employee_competencies SET employee_id = ?, competency_id = ?, level = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                employeeCompetency.getEmployee() != null ? employeeCompetency.getEmployee().getId() : null,
                employeeCompetency.getCompetency() != null ? employeeCompetency.getCompetency().getId() : null,
                employeeCompetency.getLevel() != null ? employeeCompetency.getLevel().name() : null,
                employeeCompetency.getId());
    }

    // Method to delete an employee competency
    public void deleteEmployeeCompetency(Integer id) {
        String sql = "DELETE FROM employee_competencies WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
