package vn.ptit.hrms.dao.primary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.Position;
import vn.ptit.hrms.mapper.PositionRowMapper;

import java.util.List;

@Repository
public class PositionDao {
    private final JdbcTemplate jdbcTemplate;
    private final PositionRowMapper positionRowMapper;

    public PositionDao(JdbcTemplate jdbcTemplate, PositionRowMapper positionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.positionRowMapper = positionRowMapper;
    }

   
    public void createPosition(Position position) {
        String sql = "INSERT INTO Positions (PositionName, DepartmentID) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                position.getPositionName(),
                position.getDepartment() != null ? position.getDepartment().getId() : null);
    }

   
    public Position getPositionById(Integer id) {
        String sql = "SELECT * FROM Positions WHERE PositionID = ?";
        return jdbcTemplate.queryForObject(sql, positionRowMapper, id);
    }

   
    public List<Position> getAllPositions() {
        String sql = "SELECT * FROM Positions";
        return jdbcTemplate.query(sql, positionRowMapper);
    }

   
    public void updatePosition(Position position) {
        String sql = "UPDATE Positions SET PositionName = ?, DepartmentID = ? WHERE PositionID = ?";
        jdbcTemplate.update(sql,
                position.getPositionName(),
                position.getDepartment() != null ? position.getDepartment().getId() : null,
                position.getId());
    }

   
    public void deletePosition(Integer id) {
        String sql = "DELETE FROM Positions WHERE PositionID = ?";
        jdbcTemplate.update(sql, id);
    }
}