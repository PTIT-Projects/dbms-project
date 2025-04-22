package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.DimEmployee;
import vn.ptit.hrms.mapper.warehouse.DimEmployeeRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

@Repository
public class DimEmployeeDao {
    private final JdbcTemplate jdbcTemplate;

    public DimEmployeeDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DimEmployee> getEmployees() {
        String sql = "SELECT * FROM dim_employees"; // Corrected table name
        return jdbcTemplate.query(sql, new DimEmployeeRowMapper());
    }

    public List<DimEmployee> getEmployeesByDepartment(Integer departmentSk) { // Corrected parameter name
        String sql = "SELECT * FROM dim_employees WHERE department_sk = ?"; // Corrected table and column name
        return jdbcTemplate.query(sql, new DimEmployeeRowMapper(), departmentSk);
    }
    public List<DimEmployee> getEmployeesByDepartmentName(String departmentName) {
        String sql = "SELECT * FROM dim_employees WHERE department_name = ?";
        return jdbcTemplate.query(sql, new DimEmployeeRowMapper(), departmentName);
    }
    public List<DimEmployee> getEmployeesByTotalYearsWorked(double totalYearsWorked) {
        String sql = "SELECT * FROM dim_employees WHERE total_years_worked = ?";
        return jdbcTemplate.query(sql, new DimEmployeeRowMapper(), totalYearsWorked);
    }


    public List<DimEmployee> getEmployeesByPositionSk(Integer positionSk) {
        String sql = "SELECT * FROM dim_employees WHERE position_sk = ?";
        return jdbcTemplate.query(sql, new DimEmployeeRowMapper(), positionSk);
    }

    public List<DimEmployee> getEmployeesByPositionName(String positionName) {
        String sql = "SELECT * FROM dim_employees WHERE position_name = ?";
        return jdbcTemplate.query(sql, new DimEmployeeRowMapper(), positionName);
    }

    /*public static void main(String[] args) {
        DimEmployeeDao dim = new DimEmployeeDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate);
        for(DimEmployee i : dim.getEmployeesByPositionName("dao")) {
            System.out.printf(i.toString());
        }
    }*/
}

