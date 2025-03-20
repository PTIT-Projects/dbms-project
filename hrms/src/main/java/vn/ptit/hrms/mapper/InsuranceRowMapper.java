package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Insurance;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee

public class InsuranceRowMapper implements RowMapper<Insurance> {

    private final EmployeeDAO employeeDAO;

    public InsuranceRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Insurance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Insurance insurance = new Insurance();

        // Map Insurance id
        insurance.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        Employee employee = employeeDAO.findById(employeeId);
        insurance.setEmployee(employee);

        // Map Insurance number
        insurance.setInsuranceNumber(rs.getString("insurance_number"));

        // Map Insurance type
        insurance.setInsuranceType(rs.getString("insurance_type"));

        // Map LocalDate field from SQL Date for start date
        Date sqlStartDate = rs.getDate("start_date");
        if (sqlStartDate != null) {
            insurance.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map LocalDate field from SQL Date for end date
        Date sqlEndDate = rs.getDate("end_date");
        if (sqlEndDate != null) {
            insurance.setEndDate(sqlEndDate.toLocalDate());
        }

        return insurance;
    }
}
