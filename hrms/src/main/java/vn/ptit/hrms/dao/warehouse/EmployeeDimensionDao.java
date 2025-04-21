package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.DimEmployee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeDimensionDao {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDimensionDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DimEmployee> getCurrentEmployees() {
        String sql = "SELECT * FROM dim_employee WHERE is_current = 1";
        return jdbcTemplate.query(sql, new EmployeeDimensionRowMapper());
    }

    public List<DimEmployee> getEmployeesByDepartment(Integer departmentId) {
        String sql = "SELECT * FROM dim_employee WHERE department_id = ? AND is_current = 1";
        return jdbcTemplate.query(sql, new EmployeeDimensionRowMapper(), departmentId);
    }

    private static class EmployeeDimensionRowMapper implements RowMapper<DimEmployee> {
        @Override
        public DimEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
            DimEmployee employee = new DimEmployee();
            employee.setEmployeeKey(rs.getInt("employee_key"));
            employee.setEmployeeId(rs.getInt("employee_id"));
            employee.setFullName(rs.getString("full_name"));
            employee.setGender(rs.getString("gender"));
            employee.setDepartmentId(rs.getInt("department_id"));
            employee.setDepartmentName(rs.getString("department_name"));
            employee.setPositionId(rs.getInt("position_id"));
            employee.setPositionName(rs.getString("position_name"));
            employee.setEmployeeStatus(rs.getString("employee_status"));
            employee.setCurrent(rs.getBoolean("is_current"));

            if (rs.getDate("effective_date") != null) {
                employee.setEffectiveDate(rs.getDate("effective_date").toLocalDate());
            }

            if (rs.getDate("expiration_date") != null) {
                employee.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
            }

            return employee;
        }
    }
}