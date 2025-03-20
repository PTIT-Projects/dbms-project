package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.DependentDeduction;
import vn.ptit.hrms.domain.Employee;

public class DependentDeductionRowMapper implements RowMapper<DependentDeduction> {

    private final EmployeeDao employeeDAO;

    public DependentDeductionRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public DependentDeduction mapRow(ResultSet rs, int rowNum) throws SQLException {
        DependentDeduction dependentDeduction = new DependentDeduction();

       
        dependentDeduction.setId(rs.getInt("DeductionID"));

       
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            dependentDeduction.setEmployee(employee);
        }

       
        dependentDeduction.setNumberOfDependents(rs.getInt("NumberOfDependents"));

       
        dependentDeduction.setDeductionAmount(rs.getDouble("DeductionAmount"));

        return dependentDeduction;
    }
}