package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import vn.ptit.hrms.constant.ApplicantStatusEnum;
import vn.ptit.hrms.dao.primary.RecruitmentPlanDao;
import vn.ptit.hrms.domain.primary.Applicant;
import vn.ptit.hrms.domain.primary.RecruitmentPlan;

public class ApplicantRowMapper implements RowMapper<Applicant> {

    private final RecruitmentPlanDao recruitmentPlanDAO;

    public ApplicantRowMapper(RecruitmentPlanDao recruitmentPlanDAO) {
        this.recruitmentPlanDAO = recruitmentPlanDAO;
    }

    @Override
    public Applicant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Applicant applicant = new Applicant();

        applicant.setId(rs.getInt("ApplicantID"));

       
        int planId = rs.getInt("PlanID");
        RecruitmentPlan plan = recruitmentPlanDAO.getRecruitmentPlanById(planId);
        applicant.setPlan(plan);

        applicant.setFullName(rs.getNString("FullName"));
        applicant.setEmail(rs.getNString("email"));
        applicant.setPhone(rs.getNString("phone"));

        String statusValue = rs.getNString("status");
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