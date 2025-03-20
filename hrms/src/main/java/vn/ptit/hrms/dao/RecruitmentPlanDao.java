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

   
    public void createRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        String sql = "INSERT INTO RecruitmentPlans (Position, DepartmentID, Quantity, StartDate, EndDate) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                recruitmentPlan.getPosition(),
                recruitmentPlan.getDepartment() != null ? recruitmentPlan.getDepartment().getId() : null,
                recruitmentPlan.getQuantity(),
                recruitmentPlan.getStartDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getStartDate()) : null,
                recruitmentPlan.getEndDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getEndDate()) : null);
    }

   
    public RecruitmentPlan getRecruitmentPlanById(Integer id) {
        String sql = "SELECT * FROM RecruitmentPlans WHERE PlanID = ?";
        return jdbcTemplate.queryForObject(sql, recruitmentPlanRowMapper, id);
    }

   
    public List<RecruitmentPlan> getAllRecruitmentPlans() {
        String sql = "SELECT * FROM RecruitmentPlans";
        return jdbcTemplate.query(sql, recruitmentPlanRowMapper);
    }

   
    public void updateRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        String sql = "UPDATE RecruitmentPlans SET Position = ?, DepartmentID = ?, Quantity = ?, StartDate = ?, EndDate = ? WHERE PlanID = ?";
        jdbcTemplate.update(sql,
                recruitmentPlan.getPosition(),
                recruitmentPlan.getDepartment() != null ? recruitmentPlan.getDepartment().getId() : null,
                recruitmentPlan.getQuantity(),
                recruitmentPlan.getStartDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getStartDate()) : null,
                recruitmentPlan.getEndDate() != null ? java.sql.Date.valueOf(recruitmentPlan.getEndDate()) : null,
                recruitmentPlan.getId());
    }

   
    public void deleteRecruitmentPlan(Integer id) {
        String sql = "DELETE FROM RecruitmentPlans WHERE PlanID = ?";
        jdbcTemplate.update(sql, id);
    }
}