package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Insurance;
import vn.ptit.hrms.dao.EmployeeDao;

public class InsuranceRowMapper implements RowMapper<Insurance> {

    private final EmployeeDao employeeDAO;

    public InsuranceRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Insurance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Insurance insurance = new Insurance();

        // Map Insurance id
        insurance.setId(rs.getInt("InsuranceID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            insurance.setEmployee(employee);
        }

        // Map Insurance number
        insurance.setInsuranceNumber(rs.getString("InsuranceNumber"));

        // Map Insurance type
        insurance.setInsuranceType(rs.getString("InsuranceType"));

        // Map LocalDate field from SQL Date for start date
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            insurance.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map LocalDate field from SQL Date for end date
        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            insurance.setEndDate(sqlEndDate.toLocalDate());
        }

        return insurance;
    }
}