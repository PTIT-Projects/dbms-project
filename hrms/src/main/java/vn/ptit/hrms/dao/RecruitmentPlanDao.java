package vn.ptit.hrms.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.RecruitmentPlan;
import vn.ptit.hrms.mapper.RecruitmentPlanRowMapper;

import java.util.List;

@Repository
public class RecruitmentPlanDao {
    private final JdbcTemplate jdbcTemplate;
    private final RecruitmentPlanRowMapper recruitmentPlanRowMapper;

    public RecruitmentPlanDao(JdbcTemplate jdbcTemplate, RecruitmentPlanRowMapper recruitmentPlanRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.recruitmentPlanRowMapper = recruitmentPlanRowMapper;
    }

    // Method to create a new recruitment plan
    public void createRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        String sql = "INSERT INTO RecruitmentPlans (PositionID, DepartmentID, Quantity, StartDate, EndDate) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                recruitmentPlan.getPosition() != null ? recruitmentPlan.getPosition().getId() : null,
                recruitmentPlan.getDepartment() != null ? recruitmentPlan.getDepartment().getId() : null,
                recruitmentPlan.getQuantity(),
                recruitmentPlan.getStartDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getStartDate()) : null,
                recruitmentPlan.getEndDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getEndDate()) : null);
    }

    // Method to get a recruitment plan by ID
    public RecruitmentPlan getRecruitmentPlanById(Integer id) {
        try {
            String sql = "SELECT * FROM RecruitmentPlans WHERE PlanID = ?";
            return jdbcTemplate.queryForObject(sql, recruitmentPlanRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<RecruitmentPlan> getAllRecruitmentPlans() {
        try {
            String sql = "SELECT * FROM RecruitmentPlans";
            return jdbcTemplate.query(sql, recruitmentPlanRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Method to update a recruitment plan
    public void updateRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        String sql = "UPDATE RecruitmentPlans SET PositionID = ?, DepartmentID = ?, Quantity = ?, StartDate = ?, EndDate = ? WHERE PlanID = ?";
        jdbcTemplate.update(sql,
                recruitmentPlan.getPosition() != null ? recruitmentPlan.getPosition().getId() : null,
                recruitmentPlan.getDepartment() != null ? recruitmentPlan.getDepartment().getId() : null,
                recruitmentPlan.getQuantity(),
                recruitmentPlan.getStartDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getStartDate()) : null,
                recruitmentPlan.getEndDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getEndDate()) : null,
                recruitmentPlan.getId());
    }

    // Method to delete a recruitment plan
    public void deleteRecruitmentPlan(Integer id) {
        String updateApplicantsSql = "UPDATE Applicants SET PlanID = NULL WHERE PlanID = ?";
        jdbcTemplate.update(updateApplicantsSql, id);
        String sql = "DELETE FROM RecruitmentPlans WHERE PlanID = ?";
        jdbcTemplate.update(sql, id);
    }
}