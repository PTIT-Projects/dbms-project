package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.WorkTripRequest;

public class WorkTripRequestRowMapper implements RowMapper<WorkTripRequest> {

    private final EmployeeDao employeeDAO;

    public WorkTripRequestRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public WorkTripRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkTripRequest workTripRequest = new WorkTripRequest();

        // Map WorkTripRequest id
        workTripRequest.setId(rs.getInt("RequestID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        workTripRequest.setEmployee(employee);

        // Map destination
        workTripRequest.setDestination(rs.getString("Destination"));

        // Map LocalDate fields from SQL Date
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            workTripRequest.setStartDate(sqlStartDate.toLocalDate());
        }

        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            workTripRequest.setEndDate(sqlEndDate.toLocalDate());
        }

        // Map purpose
        workTripRequest.setPurpose(rs.getString("Purpose"));

        // Map RegistrationStatusEnum field using a helper method
        String statusValue = rs.getString("Status");
        if (statusValue != null) {
            workTripRequest.setStatus(getRegistrationStatus(statusValue));
        }

        return workTripRequest;
    }

    /**
     * Converts a string value from the database into the corresponding RegistrationStatusEnum.
     */
    private RegistrationStatusEnum getRegistrationStatus(String value) {
        for (RegistrationStatusEnum status : RegistrationStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) { // Changed from name() to getValue()
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown registration status value: " + value);
    }
}