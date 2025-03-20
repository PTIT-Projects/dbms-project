package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.domain.RecruitmentPlan; // Assuming you have a RecruitmentPlan class
import vn.ptit.hrms.constant.ApplicantStatusEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApplicantDao {
    private final JdbcTemplate jdbcTemplate;

    public ApplicantDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to create a new applicant
    public void createApplicant(Applicant applicant) {
        String sql = "INSERT INTO applicants (plan_id, full_name, email, phone, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, applicant.getPlan().getId(), applicant.getFullName(), applicant.getEmail(), applicant.getPhone(), applicant.getStatus().name());
    }

    // Method to get an applicant by ID
    public Applicant getApplicantById(Integer id) {
        String sql = "SELECT * FROM applicants WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ApplicantRowMapper(), id);
    }

    // Method to get all applicants
    public List<Applicant> getAllApplicants() {
        String sql = "SELECT * FROM applicants";
        return jdbcTemplate.query(sql, new ApplicantRowMapper());
    }

    // Method to update an applicant
    public void updateApplicant(Applicant applicant) {
        String sql = "UPDATE applicants SET plan_id = ?, full_name = ?, email = ?, phone = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, applicant.getPlan().getId(), applicant.getFullName(), applicant.getEmail(), applicant.getPhone(), applicant.getStatus().name(), applicant.getId());
    }

    // Method to delete an applicant
    public void deleteApplicant(Integer id) {
        String sql = "DELETE FROM applicants WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMapper for Applicant
    private static class ApplicantRowMapper implements RowMapper<Applicant> {
        @Override
        public Applicant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Applicant applicant = new Applicant();
            applicant.setId(rs.getInt("id"));

            // Assuming you have a method to fetch RecruitmentPlan by ID
            RecruitmentPlan plan = new RecruitmentPlan(); // Replace with actual fetching logic
            plan.setId(rs.getInt("plan_id"));
            applicant.setPlan(plan);

            applicant.setFullName(rs.getString("full_name"));
            applicant.setEmail(rs.getString("email"));
            applicant.setPhone(rs.getString("phone"));

            String statusValue = rs.getString("status");
            if (statusValue != null) {
                applicant.setStatus(ApplicantStatusEnum.valueOf(statusValue));
            }

            return applicant;
        }
    }
}
