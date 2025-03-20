package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Registration;
import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;
import vn.ptit.hrms.dao.EmployeeDao;

public class RegistrationRowMapper implements RowMapper<Registration> {

    private final EmployeeDao employeeDAO;

    public RegistrationRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Registration mapRow(ResultSet rs, int rowNum) throws SQLException {
        Registration registration = new Registration();

        // Map Registration id
        registration.setId(rs.getInt("RegistrationID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            registration.setEmployee(employee);
        }

        // Map RegistrationType field
        String registrationTypeValue = rs.getString("RegistrationType");
        if (registrationTypeValue != null) {
            registration.setRegistrationType(getRegistrationTypeEnum(registrationTypeValue));
        }

        // Map RequestDate
        Date sqlRequestDate = rs.getDate("RequestDate");
        if (sqlRequestDate != null) {
            registration.setRequestDate(sqlRequestDate.toLocalDate());
        }

        // Map Details
        registration.setDetails(rs.getString("Details"));

        // Map Status field
        String statusValue = rs.getString("Status");
        if (statusValue != null) {
            registration.setStatus(getRegistrationStatusEnum(statusValue));
        }

        // Retrieve approved by employee id
        int approvedById = rs.getInt("ApprovedBy");
        if (approvedById > 0) {
            Employee approvedBy = employeeDAO.getEmployeeById(approvedById);
            registration.setApprovedBy(approvedBy);
        }

        return registration;
    }

    /**
     * Converts a string value from the database into the corresponding RegistrationTypeEnum.
     */
    private RegistrationTypeEnum getRegistrationTypeEnum(String value) {
        for (RegistrationTypeEnum type : RegistrationTypeEnum.values()) {
            if (type.getValue().equalsIgnoreCase(value)) { // Changed from name() to getValue()
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
            if (status.getValue().equalsIgnoreCase(value)) { // Changed from name() to getValue()
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown registration status value: " + value);
    }
}