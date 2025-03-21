package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.mapper.DepartmentRowMapper;

import java.util.List;

@Repository
public class DepartmentDao {
    private final JdbcTemplate jdbcTemplate;
    private final DepartmentRowMapper departmentRowMapper;

    public DepartmentDao(JdbcTemplate jdbcTemplate, DepartmentRowMapper departmentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.departmentRowMapper = departmentRowMapper;
    }

    public void createDepartment(Department department) {
        String sql = "INSERT INTO departments (DepartmentName) VALUES (?)";
        jdbcTemplate.update(sql, department.getDepartmentName());
    }

    public Department getDepartmentById(Integer id) {
        String sql = "SELECT * FROM Departments WHERE DepartmentID = ?";
        return jdbcTemplate.queryForObject(sql, departmentRowMapper, id);
    }

    public List<Department> getAllDepartments() {
        String sql = "SELECT * FROM departments";
        return jdbcTemplate.query(sql, departmentRowMapper);
    }

    public void updateDepartment(Department department) {
        String sql = "UPDATE departments SET DepartmentName = ? WHERE DepartmentID = ?";
        jdbcTemplate.update(sql, department.getDepartmentName(),
                department.getId());
    }

    public void deleteDepartment(Integer id) {
        String sql = "DELETE FROM departments WHERE DepartmentID = ?";
        jdbcTemplate.update(sql, id);
    }
}
