package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Decision;
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

    // Method to create a new decision
    public void createDecision(Decision decision) {
        String sql = "INSERT INTO decisions (employee_id, decision_type, decision_date, details) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, decision.getEmployee().getId(), decision.getDecisionType().name(), decision.getDecisionDate(), decision.getDetails());
    }

    // Method to get a decision by ID
    public Decision getDecisionById(Integer id) {
        String sql = "SELECT * FROM decisions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, decisionRowMapper, id);
    }

    // Method to get all decisions
    public List<Decision> getAllDecisions() {
        String sql = "SELECT * FROM decisions";
        return jdbcTemplate.query(sql, decisionRowMapper);
    }

    // Method to update a decision
    public void updateDecision(Decision decision) {
        String sql = "UPDATE decisions SET employee_id = ?, decision_type = ?, decision_date = ?, details = ? WHERE id = ?";
        jdbcTemplate.update(sql, decision.getEmployee().getId(), decision.getDecisionType().name(), decision.getDecisionDate(), decision.getDetails(), decision.getId());
    }

    // Method to delete a decision
    public void deleteDecision(Integer id) {
        String sql = "DELETE FROM decisions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
