package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.ApplicantStatusEnum;
import vn.ptit.hrms.domain.warehouse.FactApplication;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactApplicationRowMapper implements RowMapper<FactApplication> {
    @Override
    public FactApplication mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactApplication factApplication = new FactApplication();
        factApplication.setApplicantId(rs.getInt("application_id"));
        factApplication.setApplicationSk(rs.getInt("application_sk"));
        factApplication.setRecruitmentSk(rs.getInt("recruitment_sk"));
        factApplication.setApplicationDateSk(rs.getInt("application_date_sk"));
        factApplication.setPositionName(rs.getNString("position_name"));
        factApplication.setDepartmentName(rs.getNString("department_name"));
        String status = rs.getNString("status");
        factApplication.setStatus(getApplicantStatusEnum(status));
        return factApplication;
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
