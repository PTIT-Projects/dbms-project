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

        // Map DependentDeduction id - use the correct column name DeductionID
        dependentDeduction.setId(rs.getInt("DeductionID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) { // Assuming EmployeeID is nullable and 0 means no employee
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            dependentDeduction.setEmployee(employee);
        }

        // Map number of dependents - use the correct column name NumberOfDependents
        dependentDeduction.setNumberOfDependents(rs.getInt("NumberOfDependents"));

        // Map deduction amount - use the correct column name DeductionAmount
        dependentDeduction.setDeductionAmount(rs.getDouble("DeductionAmount"));

        return dependentDeduction;
    }
}