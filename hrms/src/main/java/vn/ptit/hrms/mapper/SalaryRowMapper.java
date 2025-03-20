package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Salary;
import vn.ptit.hrms.dao.EmployeeDao;

public class SalaryRowMapper implements RowMapper<Salary> {

    private final EmployeeDao employeeDAO;

    public SalaryRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
        Salary salary = new Salary();

        // Map Salary id
        salary.setId(rs.getInt("SalaryID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            salary.setEmployee(employee);
        }

        // Map month
        salary.setMonth(rs.getInt("Month"));

        // Map year
        salary.setYear(rs.getInt("Year"));

        // Map basic salary
        salary.setBasicSalary(rs.getDouble("BasicSalary"));

        // Map allowance
        salary.setAllowance(rs.getDouble("Allowance"));

        // Map deductions
        salary.setDeductions(rs.getDouble("Deductions"));

        // Map net salary
        salary.setNetSalary(rs.getDouble("NetSalary"));

        return salary;
    }
}