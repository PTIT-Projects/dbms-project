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

       
        workTripRequest.setId(rs.getInt("RequestID"));

       
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        workTripRequest.setEmployee(employee);

       
        workTripRequest.setDestination(rs.getNString("Destination"));

       
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            workTripRequest.setStartDate(sqlStartDate.toLocalDate());
        }

        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            workTripRequest.setEndDate(sqlEndDate.toLocalDate());
        }

       
        workTripRequest.setPurpose(rs.getNString("Purpose"));

       
        String statusValue = rs.getNString("Status");
        if (statusValue != null) {
            workTripRequest.setStatus(getRegistrationStatus(statusValue));
        }

        return workTripRequest;
    }


    private RegistrationStatusEnum getRegistrationStatus(String value) {
        for (RegistrationStatusEnum status : RegistrationStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown registration status value: " + value);
    }
}