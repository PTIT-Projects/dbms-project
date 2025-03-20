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
        String sql = "INSERT INTO Decisions (EmployeeID, DecisionType, DecisionDate, Details) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                decision.getEmployee().getId(),
                decision.getDecisionType().getValue(),
                decision.getDecisionDate(),
                decision.getDetails());
    }

    // Method to get a decision by ID
    public Decision getDecisionById(Integer id) {
        String sql = "SELECT * FROM Decisions WHERE DecisionID = ?";
        return jdbcTemplate.queryForObject(sql, decisionRowMapper, id);
    }

    // Method to get all decisions
    public List<Decision> getAllDecisions() {
        String sql = "SELECT * FROM Decisions";
        return jdbcTemplate.query(sql, decisionRowMapper);
    }

    // Method to update a decision
    public void updateDecision(Decision decision) {
        String sql = "UPDATE Decisions SET EmployeeID = ?, DecisionType = ?, DecisionDate = ?, Details = ? WHERE DecisionID = ?";
        jdbcTemplate.update(sql,
                decision.getEmployee().getId(),
                decision.getDecisionType().getValue(),
                decision.getDecisionDate(),
                decision.getDetails(),
                decision.getId());
    }

    // Method to delete a decision
    public void deleteDecision(Integer id) {
        String sql = "DELETE FROM Decisions WHERE DecisionID = ?";
        jdbcTemplate.update(sql, id);
    }
}