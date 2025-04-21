package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.DimPosition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DimPositionDao {
    private final JdbcTemplate jdbcTemplate;

    public DimPositionDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DimPosition getPositionByPositionSk(Integer positionSk) {
        String sql = "SELECT * FROM dim_positions WHERE position_sk = ?";
        return jdbcTemplate.queryForObject(sql, new DimPositionRowMapper(), positionSk);
    }

    public DimPosition getPositionByPositionId(Integer positionId) {
        String sql = "SELECT * FROM dim_positions WHERE position_id = ? AND is_current = 1";
        return jdbcTemplate.queryForObject(sql, new DimPositionRowMapper(), positionId);
    }

    public List<DimPosition> getPositionsByDepartmentSk(Integer departmentSk) {
        String sql = "SELECT * FROM dim_positions WHERE department_sk = ? AND is_current = 1";
        return jdbcTemplate.query(sql, new DimPositionRowMapper(), departmentSk);
    }

    public List<DimPosition> getAllCurrentPositions() {
        String sql = "SELECT * FROM dim_positions WHERE is_current = 1";
        return jdbcTemplate.query(sql, new DimPositionRowMapper());
    }

    private static class DimPositionRowMapper implements RowMapper<DimPosition> {
        @Override
        public DimPosition mapRow(ResultSet rs, int rowNum) throws SQLException {
            DimPosition position = new DimPosition();
            position.setPositionSk(rs.getInt("position_sk"));
            position.setPositionId(rs.getInt("position_id"));
            position.setPositionName(rs.getString("position_name"));
            position.setDepartmentSk(rs.getInt("department_sk"));
            position.setDepartmentName(rs.getString("department_name"));
            return position;
        }
    }
}