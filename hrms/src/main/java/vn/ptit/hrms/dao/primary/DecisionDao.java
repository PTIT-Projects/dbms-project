package vn.ptit.hrms.dao.primary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.Decision;
import vn.ptit.hrms.mapper.DecisionRowMapper;

import java.util.List;

@Repository
public class DecisionDao {
    private final JdbcTemplate jdbcTemplate;
    private final DecisionRowMapper decisionRowMapper;

    public DecisionDao(JdbcTemplate jdbcTemplate, DecisionRowMapper decisionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.decisionRowMapper = decisionRowMapper;
    }

    public void createDecision(Decision decision) {
        String sql = "INSERT INTO Decisions (EmployeeID, DecisionType, DecisionDate, Details) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                decision.getEmployee().getId(),
                decision.getDecisionType().getValue(),
                decision.getDecisionDate(),
                decision.getDetails());
    }

    public Decision getDecisionById(Integer id) {
        String sql = "SELECT * FROM Decisions WHERE DecisionID = ?";
        return jdbcTemplate.queryForObject(sql, decisionRowMapper, id);
    }

    public List<Decision> getAllDecisions() {
        String sql = "SELECT * FROM Decisions";
        return jdbcTemplate.query(sql, decisionRowMapper);
    }

    public void updateDecision(Decision decision) {
        String sql = "UPDATE Decisions SET EmployeeID = ?, DecisionType = ?, DecisionDate = ?, Details = ? WHERE DecisionID = ?";
        jdbcTemplate.update(sql,
                decision.getEmployee().getId(),
                decision.getDecisionType().getValue(),
                decision.getDecisionDate(),
                decision.getDetails(),
                decision.getId());
    }

    public void deleteDecision(Integer id) {
        String sql = "DELETE FROM Decisions WHERE DecisionID = ?";
        jdbcTemplate.update(sql, id);
    }
}