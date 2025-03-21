package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.DepartmentManager;
import vn.ptit.hrms.mapper.DepartmentManagerRowMapper;

import java.util.List;

@Repository
public class DepartmentManagerDao {
    private final JdbcTemplate jdbcTemplate;
    private final DepartmentManagerRowMapper departmentManagerRowMapper;

    public DepartmentManagerDao(JdbcTemplate jdbcTemplate, DepartmentManagerRowMapper departmentManagerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.departmentManagerRowMapper = departmentManagerRowMapper;
    }

  
    public void createDepartmentManager(DepartmentManager departmentManager) {
        String sql = "INSERT INTO DepartmentManager (DepartmentID, ManagerID) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                departmentManager.getDepartment() != null ? departmentManager.getDepartment().getId() : null,
                departmentManager.getManager() != null ? departmentManager.getManager().getId() : null);
    }

  
    public DepartmentManager getDepartmentManagerByDepartmentId(Integer departmentId) {
        String sql = "SELECT * FROM DepartmentManager WHERE DepartmentID = ?";
        return jdbcTemplate.queryForObject(sql, departmentManagerRowMapper, departmentId);
    }

  
    public List<DepartmentManager> getAllDepartmentManagers() {
        String sql = "SELECT * FROM DepartmentManager";
        return jdbcTemplate.query(sql, departmentManagerRowMapper);
    }

  
    public void updateDepartmentManager(DepartmentManager departmentManager) {
        String sql = "UPDATE DepartmentManager SET ManagerID = ? WHERE DepartmentID = ?";
        jdbcTemplate.update(sql,
                departmentManager.getManager() != null ? departmentManager.getManager().getId() : null,
                departmentManager.getDepartment() != null ? departmentManager.getDepartment().getId() : null);
    }

  
    public void deleteDepartmentManager(Integer departmentId) {
        String sql = "DELETE FROM DepartmentManager WHERE DepartmentID = ?";
        jdbcTemplate.update(sql, departmentId);
    }
}