package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Competency;
import vn.ptit.hrms.domain.Position; // Assuming you have a Position class
import vn.ptit.hrms.constant.CompetencyTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CompetencyDao {
    private final JdbcTemplate jdbcTemplate;

    public CompetencyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to create a new competency
    public void createCompetency(Competency competency) {
        String sql = "INSERT INTO competencies (position_id, competency_type, description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, competency.getPosition().getId(), competency.getCompetencyType().name(), competency.getDescription());
    }

    // Method to get a competency by ID
    public Competency getCompetencyById(Integer id) {
        String sql = "SELECT * FROM competencies WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new CompetencyRowMapper(), id);
    }

    // Method to get all competencies
    public List<Competency> getAllCompetencies() {
        String sql = "SELECT * FROM competencies";
        return jdbcTemplate.query(sql, new CompetencyRowMapper());
    }

    // Method to update a competency
    public void updateCompetency(Competency competency) {
        String sql = "UPDATE competencies SET position_id = ?, competency_type = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, competency.getPosition().getId(), competency.getCompetencyType().name(), competency.getDescription(), competency.getId());
    }

    // Method to delete a competency
    public void deleteCompetency(Integer id) {
        String sql = "DELETE FROM competencies WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMapper for Competency
    private static class CompetencyRowMapper implements RowMapper<Competency> {
        @Override
        public Competency mapRow(ResultSet rs, int rowNum) throws SQLException {
            Competency competency = new Competency();
            competency.setId(rs.getInt("id"));

            // Assuming you have a method to fetch Position by ID
            Position position = new Position(); // Replace with actual fetching logic
            position.setId(rs.getInt("position_id"));
            competency.setPosition(position);

            String competencyTypeValue = rs.getString("competency_type");
            if (competencyTypeValue != null) {
                competency.setCompetencyType(CompetencyTypeEnum.valueOf(competencyTypeValue));
            }

            competency.setDescription(rs.getString("description"));

            return competency;
        }
    }
}
