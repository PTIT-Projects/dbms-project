package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Registration;
import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee

public class RegistrationRowMapper implements RowMapper<Registration> {

    private final EmployeeDAO employeeDAO;

    public RegistrationRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Registration mapRow(ResultSet rs, int rowNum) throws SQLException {
        Registration registration = new Registration();

        // Map Registration id
        registration.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        if (employeeId > 0) { // Assuming employee ID is positive
            Employee employee = employeeDAO.findById(employeeId);
            registration.setEmployee(employee);
        }

        // Map RegistrationTypeEnum field
        String registrationTypeValue = rs.getString("registration_type");
        if (registrationTypeValue != null) {
            registration.setRegistrationType(getRegistrationTypeEnum(registrationTypeValue));
        }

        // Map LocalDate field from SQL Date for request date
        Date sqlRequestDate = rs.getDate("request_date");
        if (sqlRequestDate != null) {
            registration.setRequestDate(sqlRequestDate.toLocalDate());
        }

        // Map details
        registration.setDetails(rs.getString("details"));

        // Map RegistrationStatusEnum field
        String statusValue = rs.getString("status");
        if (statusValue != null) {
            registration.setStatus(getRegistrationStatusEnum(statusValue));
        }

        // Retrieve approved by employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int approvedById = rs.getInt("approved_by");
        if (approvedById > 0) { // Assuming employee ID is positive
            Employee approvedBy = employeeDAO.findById(approvedById);
            registration.setApprovedBy(approvedBy);
        }

        return registration;
    }

    /**
     * Converts a string value from the database into the corresponding RegistrationTypeEnum.
     */
    private RegistrationTypeEnum getRegistrationTypeEnum(String value) {
        for (RegistrationTypeEnum type : RegistrationTypeEnum.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown registration type value: " + value);
    }

    /**
     * Converts a string value from the database into the corresponding RegistrationStatusEnum.
     */
    private RegistrationStatusEnum getRegistrationStatusEnum(String value) {
        for (RegistrationStatusEnum status : RegistrationStatusEnum.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown registration status value: " + value);
    }
}
