package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import vn.ptit.hrms.constant.ApplicantStatusEnum;
import vn.ptit.hrms.dao.RecruitmentPlanDao;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.domain.RecruitmentPlan;

public class ApplicantRowMapper implements RowMapper<Applicant> {

    private final RecruitmentPlanDao recruitmentPlanDAO;

    public ApplicantRowMapper(RecruitmentPlanDao recruitmentPlanDAO) {
        this.recruitmentPlanDAO = recruitmentPlanDAO;
    }

    @Override
    public Applicant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Applicant applicant = new Applicant();

        applicant.setId(rs.getInt("id"));

        // Fetch RecruitmentPlan using DAO
        int planId = rs.getInt("plan_id");
        RecruitmentPlan plan = recruitmentPlanDAO.getRecruitmentPlanById(planId);
        applicant.setPlan(plan);

        applicant.setFullName(rs.getString("full_name"));
        applicant.setEmail(rs.getString("email"));
        applicant.setPhone(rs.getString("phone"));

        String statusValue = rs.getString("status");
        if (statusValue != null) {
            applicant.setStatus(getApplicantStatusEnum(statusValue));
        }

        return applicant;
    }

    private ApplicantStatusEnum getApplicantStatusEnum(String value) {
        for (ApplicantStatusEnum status : ApplicantStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}