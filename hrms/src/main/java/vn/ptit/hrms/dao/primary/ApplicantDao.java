package vn.ptit.hrms.dao.primary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.Applicant;
import vn.ptit.hrms.mapper.ApplicantRowMapper;

import java.util.ArrayList;
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

    public Applicant getApplicantById(Integer id) {
        String sql = "SELECT * FROM applicants WHERE ApplicantID = ?";
        return jdbcTemplate.queryForObject(sql, applicantRowMapper, id);
    }

    public List<Applicant> getAllApplicants() {
        String sql = "SELECT * FROM applicants";
        return jdbcTemplate.query(sql, applicantRowMapper);
    }

    public void updateApplicant(Applicant applicant) {
        String sql = "UPDATE applicants SET PlanID = ?, FullName = ?, email = ?, phone = ?, status = ? WHERE ApplicantID = ?";
        jdbcTemplate.update(sql, applicant.getPlan().getId(), applicant.getFullName(), applicant.getEmail(), applicant.getPhone(), applicant.getStatus().getValue(), applicant.getId());
    }

    public void deleteApplicant(Integer id) {
        String sql = "DELETE FROM applicants WHERE ApplicantID = ?";
        jdbcTemplate.update(sql, id);
    }
    public Page<Applicant> findApplicantPage(Pageable pageable, String search, Integer recruitmentPlanId, String status) {
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM Applicants a");
        StringBuilder dataSql = new StringBuilder("SELECT * FROM Applicants a");
        List<Object> params = new ArrayList<>();
        if (recruitmentPlanId != null || (search != null && !search.isEmpty()) || (status != null && !status.isEmpty())) {
            String whereClause = buildWhereClause(search, recruitmentPlanId, status, params);
            countSql.append(whereClause);
            dataSql.append(whereClause);
        }
        Integer total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class, params.toArray());
        dataSql.append(" ORDER BY a.ApplicantID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(pageable.getOffset());
        params.add(pageable.getPageSize());
        List<Applicant> applicants = jdbcTemplate.query(dataSql.toString(), applicantRowMapper, params.toArray());

        return new PageImpl<>(applicants, pageable, total != null ? total : 0);

    }
    private String buildWhereClause(String search, Integer recruitmentPlanId, String status, List<Object>params) {
        StringBuilder whereClause = new StringBuilder(" WHERE ");
        boolean hasCondition = false;

        if (search != null && !search.isEmpty()) {
            whereClause.append("(a.FullName LIKE ? OR a.Email LIKE ? OR a.Phone LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
            hasCondition = true;
        }
        if (recruitmentPlanId != null) {
            if (hasCondition) {
                whereClause.append(" AND ");
            }
            whereClause.append("a.PlanID = ?");
            params.add(recruitmentPlanId);
            hasCondition = true;
        }

        if (status != null && !status.isEmpty()) {
            if (hasCondition) {
                whereClause.append(" AND ");
            }
            whereClause.append("a.Status = ?");
            params.add(status);
        }
        return whereClause.toString();
    }

}
