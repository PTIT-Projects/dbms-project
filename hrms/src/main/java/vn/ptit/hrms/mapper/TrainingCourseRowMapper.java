package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.TrainingCourse;
import vn.ptit.hrms.dao.EmployeeDao;

public class TrainingCourseRowMapper implements RowMapper<TrainingCourse> {

    private final EmployeeDao employeeDAO;

    public TrainingCourseRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public TrainingCourse mapRow(ResultSet rs, int rowNum) throws SQLException {
        TrainingCourse trainingCourse = new TrainingCourse();

        // Map TrainingCourse id
        trainingCourse.setId(rs.getInt("CourseID"));

        // Map course name
        trainingCourse.setCourseName(rs.getString("CourseName"));

        // Map description
        trainingCourse.setDescription(rs.getString("Description"));

        // Map LocalDate field from SQL Date for start date
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            trainingCourse.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map LocalDate field from SQL Date for end date
        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            trainingCourse.setEndDate(sqlEndDate.toLocalDate());
        }

        // Retrieve trainer id from ResultSet and fetch the full Employee using EmployeeDAO
        int trainerId = rs.getInt("TrainerID");
        if (trainerId > 0) {
            Employee trainer = employeeDAO.getEmployeeById(trainerId);
            trainingCourse.setTrainer(trainer);
        }

        return trainingCourse;
    }
}