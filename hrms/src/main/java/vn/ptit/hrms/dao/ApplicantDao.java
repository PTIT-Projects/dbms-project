package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.domain.RecruitmentPlan; // Assuming you have a RecruitmentPlan class
import vn.ptit.hrms.constant.ApplicantStatusEnum;
import vn.ptit.hrms.mapper.ApplicantRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApplicantDao {
    private final JdbcTemplate jdbcTemplate;
    private final ApplicantRowMapper applicantRowMapper;
    public ApplicantDao(JdbcTemplate jdbcTemplate, ApplicantRowMapper applicantRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.applicantRowMapper = applicantRowMapper;
    }

    public void createApplicant(Applicant applicant) {
        String sql = "INSERT INTO applicants (PlanID, FullName, email, phone, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, applicant.getPlan().getId(), applicant.getFullName(), applicant.getEmail(), applicant.getPhone(), applicant.getStatus().getValue());
    }

    // Method to get an applicant by ID
    public Applicant getApplicantById(Integer id) {
        String sql = "SELECT * FROM applicants WHERE ApplicantID = ?";
        return jdbcTemplate.queryForObject(sql, applicantRowMapper, id);
    }

    // Method to get all applicants
    public List<Applicant> getAllApplicants() {
        String sql = "SELECT * FROM applicants";
        return jdbcTemplate.query(sql, applicantRowMapper);
    }

    // Method to update an applicant
    public void updateApplicant(Applicant applicant) {
        String sql = "UPDATE applicants SET PlanID = ?, FullName = ?, email = ?, phone = ?, status = ? WHERE ApplicantIDs = ?";
        jdbcTemplate.update(sql, applicant.getPlan().getId(), applicant.getFullName(), applicant.getEmail(), applicant.getPhone(), applicant.getStatus().getValue(), applicant.getId());
    }

    // Method to delete an applicant
    public void deleteApplicant(Integer id) {
        String sql = "DELETE FROM applicants WHERE ApplicantID = ?";
        jdbcTemplate.update(sql, id);
    }


}
