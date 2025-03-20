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

       
        trainingCourse.setId(rs.getInt("CourseID"));

       
        trainingCourse.setCourseName(rs.getString("CourseName"));

       
        trainingCourse.setDescription(rs.getString("Description"));

       
        Date sqlStartDate = rs.getDate("StartDate");
        if (sqlStartDate != null) {
            trainingCourse.setStartDate(sqlStartDate.toLocalDate());
        }

       
        Date sqlEndDate = rs.getDate("EndDate");
        if (sqlEndDate != null) {
            trainingCourse.setEndDate(sqlEndDate.toLocalDate());
        }

       
        int trainerId = rs.getInt("TrainerID");
        if (trainerId > 0) {
            Employee trainer = employeeDAO.getEmployeeById(trainerId);
            trainingCourse.setTrainer(trainer);
        }

        return trainingCourse;
    }
}