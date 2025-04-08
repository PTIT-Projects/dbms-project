package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.dao.DepartmentDao;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.DepartmentManager;
import vn.ptit.hrms.domain.Employee;

public class DepartmentManagerRowMapper implements RowMapper<DepartmentManager> {
    private final DepartmentDao departmentDao;
    private final EmployeeDao employeeDao;

    public DepartmentManagerRowMapper(DepartmentDao departmentDao, EmployeeDao employeeDao) {
        this.departmentDao = departmentDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public DepartmentManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        DepartmentManager departmentManager = new DepartmentManager();

      
        int departmentId = rs.getInt("DepartmentID");
        if (departmentId > 0) {
            Department department = departmentDao.getDepartmentById(departmentId);
            departmentManager.setDepartment(department);
        }

      
        int managerId = rs.getInt("ManagerID");
        if (managerId > 0) {
            Employee manager = employeeDao.getEmployeeById(managerId);
            departmentManager.setManager(manager);
        }

        return departmentManager;
    }
}