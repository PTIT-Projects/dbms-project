package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.FactRegistration;
import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactRegistrationRowMapper implements RowMapper<FactRegistration> {

    @Override
    public FactRegistration mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactRegistration registration = new FactRegistration();
        registration.setRegistrationSk(rs.getInt("registration_sk"));
        registration.setRegistrationId(rs.getInt("registration_id"));
        registration.setEmployeeSk(rs.getInt("employee_sk"));
        registration.setEmployeeName(rs.getString("employee_name"));
        registration.setDepartmentName(rs.getString("department_name"));
        registration.setApprovedByName(rs.getString("approved_by_name"));

        // Convert from String to Enum
        String registrationTypeString = rs.getString("registration_type");
        RegistrationTypeEnum registrationType = null;
        if (registrationTypeString != null) {
            try {
                registrationType = RegistrationTypeEnum.valueOf(registrationTypeString);
            } catch (IllegalArgumentException e) {
                // Handle the case where the string doesn't match any enum value
                // You might want to log an error or set a default value
                registrationType = null; // Or set a default enum value
            }
        }
        registration.setRegistrationType(registrationType);

        registration.setRequestDateSk(rs.getInt("request_date_sk"));
        registration.setRegistrationDuration(rs.getInt("registration_duration"));

        // Convert from String to Enum
        String statusString = rs.getString("status");
        RegistrationStatusEnum status = null;
        if (statusString != null) {
            try {
                status = RegistrationStatusEnum.valueOf(statusString);
            } catch (IllegalArgumentException e) {
                // Handle the case where the string doesn't match any enum value
                // You might want to log an error or set a default value
                status = null; // Or set a default enum value
            }
        }
        registration.setStatus(status);

        registration.setApprovedBySk(rs.getInt("approved_by_sk"));
        return registration;
    }
}
