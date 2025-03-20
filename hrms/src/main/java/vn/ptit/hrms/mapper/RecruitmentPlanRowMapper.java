package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.RecruitmentPlan;
import vn.ptit.hrms.dao.DepartmentDao;

public class RecruitmentPlanRowMapper implements RowMapper<RecruitmentPlan> {

    private final DepartmentDao departmentDAO;

    public RecruitmentPlanRowMapper(DepartmentDao departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @Override
    public RecruitmentPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecruitmentPlan recruitmentPlan = new RecruitmentPlan();

        // Map RecruitmentPlan id
        recruitmentPlan.setId(rs.getInt("PlanID"));

        // Map position
        recruitmentPlan.setPosition(rs.getString("Position"));

        // Retrieve department id from ResultSet and fetch the full Department using DepartmentDAO
        int departmentId = rs.getInt("DepartmentID");
        if (departmentId > 0) {
            Department department = departmentDAO.getDepartmentById(departmentId);
            recruitmentPlan.setDepartment(department);
        }

        // Map quantity
        recruitmentPlan.setQuantity(rs.getInt("Quantity"));

        // Map LocalDate field from SQL Date for start date
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            recruitmentPlan.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map LocalDate field from SQL Date for end date
        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            recruitmentPlan.setEndDate(sqlEndDate.toLocalDate());
        }

        return recruitmentPlan;
    }
}