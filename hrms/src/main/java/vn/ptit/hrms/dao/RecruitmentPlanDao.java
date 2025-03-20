package vn.ptit.hrms.dao;

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
        String sql = "INSERT INTO recruitment_plans (position, department_id, quantity, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                recruitmentPlan.getPosition(),
                recruitmentPlan.getDepartment() != null ? recruitmentPlan.getDepartment().getId() : null,
                recruitmentPlan.getQuantity(),
                recruitmentPlan.getStartDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getStartDate()) : null,
                recruitmentPlan.getEndDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getEndDate()) : null);
    }

    // Method to get a recruitment plan by ID
    public RecruitmentPlan getRecruitmentPlanById(Integer id) {
        String sql = "SELECT * FROM recruitment_plans WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, recruitmentPlanRowMapper, id);
    }

    // Method to get all recruitment plans
    public List<RecruitmentPlan> getAllRecruitmentPlans() {
        String sql = "SELECT * FROM recruitment_plans";
        return jdbcTemplate.query(sql, recruitmentPlanRowMapper);
    }

    // Method to update a recruitment plan
    public void updateRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        String sql = "UPDATE recruitment_plans SET position = ?, department_id = ?, quantity = ?, start_date = ?, end_date = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                recruitmentPlan.getPosition(),
                recruitmentPlan.getDepartment() != null ? recruitmentPlan.getDepartment().getId() : null,
                recruitmentPlan.getQuantity(),
                recruitmentPlan.getStartDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getStartDate()) : null,
                recruitmentPlan.getEndDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getEndDate()) : null,
                recruitmentPlan.getId());
    }

    // Method to delete a recruitment plan
    public void deleteRecruitmentPlan(Integer id) {
        String sql = "DELETE FROM recruitment_plans WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
