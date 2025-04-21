package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.primary.Department;

public class DepartmentRowMapper implements RowMapper<Department> {


    public DepartmentRowMapper() {
    }

    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        Department department = new Department();

       
        department.setId(rs.getInt("DepartmentID"));

       
        department.setDepartmentName(rs.getNString("DepartmentName"));

        return department;
    }
}