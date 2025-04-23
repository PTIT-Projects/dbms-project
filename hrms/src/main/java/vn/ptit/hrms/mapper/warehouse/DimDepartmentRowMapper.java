package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.DimDepartment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DimDepartmentRowMapper implements RowMapper<DimDepartment> {
    @Override
    public DimDepartment mapRow(ResultSet rs, int rowNum) throws SQLException {
        DimDepartment dimDepartment = new DimDepartment();
        dimDepartment.setDepartmentId(rs.getInt("department_id"));;
        dimDepartment.setDepartmentSk(rs.getInt("department_sk"));
        dimDepartment.setDepartmentName(rs.getNString("department_name"));
        dimDepartment.setManagerSk(rs.getInt("manager_sk"));
        dimDepartment.setManagerName(rs.getNString("manager_name"));
        return dimDepartment;
    }
}
