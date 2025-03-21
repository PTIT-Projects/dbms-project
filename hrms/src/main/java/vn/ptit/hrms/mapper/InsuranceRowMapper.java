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

       
        insurance.setId(rs.getInt("InsuranceID"));

       
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            insurance.setEmployee(employee);
        }

       
        insurance.setInsuranceNumber(rs.getNString("InsuranceNumber"));

       
        insurance.setInsuranceType(rs.getNString("InsuranceType"));

       
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            insurance.setStartDate(sqlStartDate.toLocalDate());
        }

       
        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            insurance.setEndDate(sqlEndDate.toLocalDate());
        }

        return insurance;
    }
}