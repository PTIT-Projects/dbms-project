package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.dao.EmployeeDAO;
import vn.ptit.hrms.domain.DependentDeduction;
import vn.ptit.hrms.domain.Employee;

public class DependentDeductionRowMapper implements RowMapper<DependentDeduction> {

    private final EmployeeDAO employeeDAO;

    public DependentDeductionRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public DependentDeduction mapRow(ResultSet rs, int rowNum) throws SQLException {
        DependentDeduction dependentDeduction = new DependentDeduction();

        // Map DependentDeduction id
        dependentDeduction.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        if (employeeId > 0) { // Assuming employee_id is nullable and 0 means no employee
            Employee employee = employeeDAO.findById(employeeId);
            dependentDeduction.setEmployee(employee);
        }

        // Map number of dependents
        dependentDeduction.setNumberOfDependents(rs.getInt("number_of_dependents"));

        // Map deduction amount
        dependentDeduction.setDeductionAmount(rs.getDouble("deduction_amount"));

        return dependentDeduction;
    }
}
