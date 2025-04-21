package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.primary.Employee;
import vn.ptit.hrms.domain.primary.Salary;
import vn.ptit.hrms.dao.primary.EmployeeDao;

public class SalaryRowMapper implements RowMapper<Salary> {

    private final EmployeeDao employeeDAO;

    public SalaryRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
        Salary salary = new Salary();

       
        salary.setId(rs.getInt("SalaryID"));

       
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            salary.setEmployee(employee);
        }

       
        salary.setMonth(rs.getInt("Month"));

       
        salary.setYear(rs.getInt("Year"));

       
        salary.setBasicSalary(rs.getDouble("BasicSalary"));

       
        salary.setAllowance(rs.getDouble("Allowance"));

       
        salary.setDeductions(rs.getDouble("Deductions"));

       
        salary.setNetSalary(rs.getDouble("NetSalary"));

        return salary;
    }
}