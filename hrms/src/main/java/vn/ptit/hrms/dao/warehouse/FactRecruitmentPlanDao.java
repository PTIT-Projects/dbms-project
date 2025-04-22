package vn.ptit.hrms.dao.warehouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.warehouse.FactRecruitmentPlan;
import vn.ptit.hrms.mapper.warehouse.FactRecruitmentPlanRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FactRecruitmentPlanDao {
    private final JdbcTemplate jdbcTemplate;

    public FactRecruitmentPlanDao(@Qualifier("warehouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FactRecruitmentPlan> getRecruitmentPlans() {
        String sql = "SELECT * FROM fact_recruitment_plan";
        return jdbcTemplate.query(sql, new FactRecruitmentPlanRowMapper());
    }

    public List<FactRecruitmentPlan> getRecruitmentPlansByDepartment(int departmentSk) {
        String sql = "SELECT * FROM fact_recruitment_plan WHERE department_sk = ?";
        return jdbcTemplate.query(sql, new FactRecruitmentPlanRowMapper(), departmentSk);
    }

    public List<FactRecruitmentPlan> getRecruitmentPlansByPosition(int positionSk) {
        String sql = "SELECT * FROM fact_recruitment_plan WHERE position_sk = ?";
        return jdbcTemplate.query(sql, new FactRecruitmentPlanRowMapper(), positionSk);
    }

    /*public List<FactRecruitmentPlan> getRecruitmentPlansByDateRange(int startDateSk, int endDateSk) {
        String sql = "SELECT * FROM fact_recruitment_plan WHERE start_date_sk >= ? AND end_date_sk <= ?";
        return jdbcTemplate.query(sql, new FactRecruitmentPlanRowMapper(), startDateSk, endDateSk);
    }*/
}

