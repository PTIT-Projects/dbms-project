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

    public void createCompetency(Competency competency) {
        String sql = "INSERT INTO Competencies (PositionID, CompetencyType, Description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                competency.getPosition().getId(),
                competency.getCompetencyType().getValue(),
                competency.getDescription());
    }

    public Competency getCompetencyById(Integer id) {
        String sql = "SELECT * FROM Competencies WHERE CompetencyID = ?";
        return jdbcTemplate.queryForObject(sql, competencyRowMapper, id);
    }

    public List<Competency> getAllCompetencies() {
        String sql = "SELECT * FROM Competencies";
        return jdbcTemplate.query(sql, competencyRowMapper);
    }

    public void updateCompetency(Competency competency) {
        String sql = "UPDATE Competencies SET PositionID = ?, CompetencyType = ?, Description = ? WHERE CompetencyID = ?";
        jdbcTemplate.update(sql,
                competency.getPosition().getId(),
                competency.getCompetencyType().getValue(),
                competency.getDescription(),
                competency.getId());
    }

    public void deleteCompetency(Integer id) {
        String sql = "DELETE FROM Competencies WHERE CompetencyID = ?";
        jdbcTemplate.update(sql, id);
    }
}