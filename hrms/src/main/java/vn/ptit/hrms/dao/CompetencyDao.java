package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Competency;
import vn.ptit.hrms.mapper.CompetencyRowMapper;

import java.util.List;

@Repository
public class CompetencyDao {
    private final JdbcTemplate jdbcTemplate;
    private final CompetencyRowMapper competencyRowMapper;

    public CompetencyDao(JdbcTemplate jdbcTemplate, CompetencyRowMapper competencyRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.competencyRowMapper = competencyRowMapper;
    }

    // Method to create a new competency
    public void createCompetency(Competency competency) {
        String sql = "INSERT INTO Competencies (PositionID, CompetencyType, Description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                competency.getPosition().getId(),
                competency.getCompetencyType().getValue(),
                competency.getDescription());
    }

    // Method to get a competency by ID
    public Competency getCompetencyById(Integer id) {
        String sql = "SELECT * FROM Competencies WHERE CompetencyID = ?";
        return jdbcTemplate.queryForObject(sql, competencyRowMapper, id);
    }

    // Method to get all competencies
    public List<Competency> getAllCompetencies() {
        String sql = "SELECT * FROM Competencies";
        return jdbcTemplate.query(sql, competencyRowMapper);
    }

    // Method to update a competency
    public void updateCompetency(Competency competency) {
        String sql = "UPDATE Competencies SET PositionID = ?, CompetencyType = ?, Description = ? WHERE CompetencyID = ?";
        jdbcTemplate.update(sql,
                competency.getPosition().getId(),
                competency.getCompetencyType().getValue(),
                competency.getDescription(),
                competency.getId());
    }

    // Method to delete a competency
    public void deleteCompetency(Integer id) {
        String sql = "DELETE FROM Competencies WHERE CompetencyID = ?";
        jdbcTemplate.update(sql, id);
    }
}