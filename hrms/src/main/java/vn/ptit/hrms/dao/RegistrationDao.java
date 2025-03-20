package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Registration;
import vn.ptit.hrms.mapper.RegistrationRowMapper;

import java.util.List;

@Repository
public class RegistrationDao {
    private final JdbcTemplate jdbcTemplate;
    private final RegistrationRowMapper registrationRowMapper;

    public RegistrationDao(JdbcTemplate jdbcTemplate, RegistrationRowMapper registrationRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.registrationRowMapper = registrationRowMapper;
    }

    // Method to create a new registration
    public void createRegistration(Registration registration) {
        String sql = "INSERT INTO Registrations (EmployeeID, RegistrationType, RequestDate, Details, Status, ApprovedBy) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                registration.getEmployee() != null ? registration.getEmployee().getId() : null,
                registration.getRegistrationType() != null ? registration.getRegistrationType().getValue() : null,
                registration.getRequestDate(),
                registration.getDetails(),
                registration.getStatus() != null ? registration.getStatus().getValue() : null,
                registration.getApprovedBy() != null ? registration.getApprovedBy().getId() : null);
    }

    // Method to get a registration by ID
    public Registration getRegistrationById(Integer id) {
        String sql = "SELECT * FROM Registrations WHERE RegistrationID = ?";
        return jdbcTemplate.queryForObject(sql, registrationRowMapper, id);
    }

    // Method to get all registrations
    public List<Registration> getAllRegistrations() {
        String sql = "SELECT * FROM Registrations";
        return jdbcTemplate.query(sql, registrationRowMapper);
    }

    // Method to update a registration
    public void updateRegistration(Registration registration) {
        String sql = "UPDATE Registrations SET EmployeeID = ?, RegistrationType = ?, RequestDate = ?, Details = ?, Status = ?, ApprovedBy = ? WHERE RegistrationID = ?";
        jdbcTemplate.update(sql,
                registration.getEmployee() != null ? registration.getEmployee().getId() : null,
                registration.getRegistrationType() != null ? registration.getRegistrationType().getValue() : null,
                registration.getRequestDate(),
                registration.getDetails(),
                registration.getStatus() != null ? registration.getStatus().getValue() : null,
                registration.getApprovedBy() != null ? registration.getApprovedBy().getId() : null,
                registration.getId());
    }

    // Method to delete a registration
    public void deleteRegistration(Integer id) {
        String sql = "DELETE FROM Registrations WHERE RegistrationID = ?";
        jdbcTemplate.update(sql, id);
    }
}