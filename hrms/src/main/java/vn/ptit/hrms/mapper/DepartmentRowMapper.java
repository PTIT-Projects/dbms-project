package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.Employee;

public class DepartmentRowMapper implements RowMapper<Department> {

    private final EmployeeDao employeeDAO;

    public DepartmentRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        Department department = new Department();

        // Map Department id
        department.setId(rs.getInt("DepartmentID"));

        // Map Department name
        department.setDepartmentName(rs.getString("DepartmentName"));

        // Retrieve manager id from ResultSet and fetch the full Employee using EmployeeDAO.
        int managerId = rs.getInt("ManagerID");
        if (managerId > 0) { // Assuming manager_id is nullable and 0 means no manager
            Employee manager = employeeDAO.getEmployeeById(managerId);
            department.setManager(manager);
        }

        return department;
    }
}