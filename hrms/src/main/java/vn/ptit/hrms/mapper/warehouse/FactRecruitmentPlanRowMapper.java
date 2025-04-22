package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.FactRecruitmentPlan;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactRecruitmentPlanRowMapper implements RowMapper<FactRecruitmentPlan> {

    @Override
    public FactRecruitmentPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactRecruitmentPlan plan = new FactRecruitmentPlan();
        plan.setRecruitmentSk(rs.getInt("recruitment_sk"));
        plan.setRecruitmentId(rs.getInt("recruitment_id"));
        plan.setPositionSk(rs.getInt("position_sk"));
        plan.setPositionName(rs.getString("position_name"));
        plan.setDepartmentSk(rs.getInt("department_sk"));
        plan.setDepartmentName(rs.getString("department_name"));
        plan.setStartDateSk(rs.getInt("start_date_sk"));
        plan.setEndDateSk(rs.getInt("end_date_sk"));
        plan.setQuantity(rs.getInt("quantity"));
        plan.setRecruitmentDuration(rs.getInt("recruitment_duration"));
        plan.setRemainingPositions(rs.getInt("remaining_positions"));
        return plan;
    }
}

