package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.DimDepartment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DimDepartmentDao {
    private final JdbcTemplate jdbcTemplate;

    public DimDepartmentDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DimDepartment getDepartmentByDepartmentSk(Integer departmentSk) {
        String sql = "SELECT * FROM dim_departments WHERE department_sk = ?";
        return jdbcTemplate.queryForObject(sql, new DimDepartmentRowMapper(), departmentSk);
    }

    public DimDepartment getDepartmentByDepartmentId(Integer departmentId) {
        String sql = "SELECT * FROM dim_departments WHERE department_id = ? AND is_current = 1";
        return jdbcTemplate.queryForObject(sql, new DimDepartmentRowMapper(), departmentId);
    }

    public List<DimDepartment> getAllCurrentDepartments() {
        String sql = "SELECT * FROM dim_departments WHERE is_current = 1";
        return jdbcTemplate.query(sql, new DimDepartmentRowMapper());
    }

    public List<DimDepartment> getAllDepartments() {
        String sql = "SELECT * FROM dim_departments";
        return jdbcTemplate.query(sql, new DimDepartmentRowMapper());
    }

    private static class DimDepartmentRowMapper implements RowMapper<DimDepartment> {
        @Override
        public DimDepartment mapRow(ResultSet rs, int rowNum) throws SQLException {
            DimDepartment department = new DimDepartment();
            department.setDepartmentSk(rs.getInt("department_sk"));
            department.setDepartmentId(rs.getInt("department_id"));
            department.setDepartmentName(rs.getString("department_name"));
            department.setManagerSk(rs.getInt("manager_sk"));
            department.setManagerName(rs.getString("manager_name"));
            return department;
        }
    }
}