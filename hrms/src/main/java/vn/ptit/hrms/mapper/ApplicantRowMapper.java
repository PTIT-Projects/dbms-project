package vn.ptit.hrms.mapper;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Applicant;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicantRowMapper implements RowMapper<Applicant> {
    @Override
    public Applicant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Applicant applicant = new Applicant();
        applicant.setId(rs.getInt("ApplicantID"));
        applicant.setFullName(rs.getString("FullName"));
        return applicant;
    }
}
