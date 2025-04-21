package vn.ptit.hrms.dao.primary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.Registration;
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

   
    public Registration getRegistrationById(Integer id) {
        String sql = "SELECT * FROM Registrations WHERE RegistrationID = ?";
        return jdbcTemplate.queryForObject(sql, registrationRowMapper, id);
    }

   
    public List<Registration> getAllRegistrations() {
        String sql = "SELECT * FROM Registrations";
        return jdbcTemplate.query(sql, registrationRowMapper);
    }

   
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

   
    public void deleteRegistration(Integer id) {
        String sql = "DELETE FROM Registrations WHERE RegistrationID = ?";
        jdbcTemplate.update(sql, id);
    }
    public int countPendingRegistration() {
        String sql = "SELECT COUNT(*) FROM Registrations WHERE Status = N'Đang chờ'";
        Integer res = jdbcTemplate.queryForObject(sql, Integer.class);
        return (res != null) ? res : 0;
    }
}