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

       
        department.setId(rs.getInt("DepartmentID"));

       
        department.setDepartmentName(rs.getString("DepartmentName"));

       
        int managerId = rs.getInt("ManagerID");
        if (managerId > 0) {
            Employee manager = employeeDAO.getEmployeeById(managerId);
            department.setManager(manager);
        }

        return department;
    }
}