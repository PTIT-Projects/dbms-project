package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.primary.Employee;
import vn.ptit.hrms.domain.primary.Registration;
import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;
import vn.ptit.hrms.dao.primary.EmployeeDao;

public class RegistrationRowMapper implements RowMapper<Registration> {

    private final EmployeeDao employeeDAO;

    public RegistrationRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Registration mapRow(ResultSet rs, int rowNum) throws SQLException {
        Registration registration = new Registration();

       
        registration.setId(rs.getInt("RegistrationID"));

       
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            registration.setEmployee(employee);
        }

       
        String registrationTypeValue = rs.getNString("RegistrationType");
        if (registrationTypeValue != null) {
            registration.setRegistrationType(getRegistrationTypeEnum(registrationTypeValue));
        }

       
        Date sqlRequestDate = rs.getDate("RequestDate");
        if (sqlRequestDate != null) {
            registration.setRequestDate(sqlRequestDate.toLocalDate());
        }

       
        registration.setDetails(rs.getNString("Details"));

       
        String statusValue = rs.getNString("Status");
        if (statusValue != null) {
            registration.setStatus(getRegistrationStatusEnum(statusValue));
        }

       
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
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown registration type value: " + value);
    }


    private RegistrationStatusEnum getRegistrationStatusEnum(String value) {
        for (RegistrationStatusEnum status : RegistrationStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown registration status value: " + value);
    }
}