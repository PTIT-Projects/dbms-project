package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.RecruitmentPlan;
import vn.ptit.hrms.dao.DepartmentDAO; // Assuming you have a DepartmentDAO to fetch Department

public class RecruitmentPlanRowMapper implements RowMapper<RecruitmentPlan> {

    private final DepartmentDAO departmentDAO;

    public RecruitmentPlanRowMapper(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @Override
    public RecruitmentPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
        RecruitmentPlan recruitmentPlan = new RecruitmentPlan();

        // Map RecruitmentPlan id
        recruitmentPlan.setId(rs.getInt("id"));

        // Map position
        recruitmentPlan.setPosition(rs.getString("position"));

        // Retrieve department id from ResultSet and fetch the full Department using DepartmentDAO.
        int departmentId = rs.getInt("department_id"); // Assuming the column name is department_id
        if (departmentId > 0) { // Assuming department ID is positive
            Department department = departmentDAO.findById(departmentId);
            recruitmentPlan.setDepartment(department);
        }

        // Map quantity
        recruitmentPlan.setQuantity(rs.getInt("quantity"));

        // Map LocalDate field from SQL Date for start date
        Date sqlStartDate = rs.getDate("start_date");
        if (sqlStartDate != null) {
            recruitmentPlan.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map LocalDate field from SQL Date for end date
        Date sqlEndDate = rs.getDate("end_date");
        if (sqlEndDate != null) {
            recruitmentPlan.setEndDate(sqlEndDate.toLocalDate());
        }

        return recruitmentPlan;
    }
}
