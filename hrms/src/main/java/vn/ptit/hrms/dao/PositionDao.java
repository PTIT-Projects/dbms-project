package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Position;
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

    // Method to create a new position
    public void createPosition(Position position) {
        String sql = "INSERT INTO Positions (PositionName, DepartmentID) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                position.getPositionName(),
                position.getDepartment() != null ? position.getDepartment().getId() : null);
    }

    // Method to get a position by ID
    public Position getPositionById(Integer id) {
        String sql = "SELECT * FROM Positions WHERE PositionID = ?";
        return jdbcTemplate.queryForObject(sql, positionRowMapper, id);
    }

    // Method to get all positions
    public List<Position> getAllPositions() {
        String sql = "SELECT * FROM Positions";
        return jdbcTemplate.query(sql, positionRowMapper);
    }

    // Method to update a position
    public void updatePosition(Position position) {
        String sql = "UPDATE Positions SET PositionName = ?, DepartmentID = ? WHERE PositionID = ?";
        jdbcTemplate.update(sql,
                position.getPositionName(),
                position.getDepartment() != null ? position.getDepartment().getId() : null,
                position.getId());
    }

    // Method to delete a position
    public void deletePosition(Integer id) {
        String sql = "DELETE FROM Positions WHERE PositionID = ?";
        jdbcTemplate.update(sql, id);
    }
}