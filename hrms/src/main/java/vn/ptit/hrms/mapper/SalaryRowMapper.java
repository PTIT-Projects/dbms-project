package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Salary;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee

public class SalaryRowMapper implements RowMapper<Salary> {

    private final EmployeeDAO employeeDAO;

    public SalaryRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
        Salary salary = new Salary();

        // Map Salary id
        salary.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        if (employeeId > 0) { // Assuming employee ID is positive
            Employee employee = employeeDAO.findById(employeeId);
            salary.setEmployee(employee);
        }

        // Map month
        salary.setMonth(rs.getInt("month"));

        // Map year
        salary.setYear(rs.getInt("year"));

        // Map basic salary
        salary.setBasicSalary(rs.getDouble("basic_salary"));

        // Map allowance
        salary.setAllowance(rs.getDouble("allowance"));

        // Map deductions
        salary.setDeductions(rs.getDouble("deductions"));

        // Map net salary
        salary.setNetSalary(rs.getDouble("net_salary"));

        return salary;
    }
}
