package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.TrainingCourse;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee

public class TrainingCourseRowMapper implements RowMapper<TrainingCourse> {

    private final EmployeeDAO employeeDAO;

    public TrainingCourseRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public TrainingCourse mapRow(ResultSet rs, int rowNum) throws SQLException {
        TrainingCourse trainingCourse = new TrainingCourse();

        // Map TrainingCourse id
        trainingCourse.setId(rs.getInt("id"));

        // Map course name
        trainingCourse.setCourseName(rs.getString("course_name"));

        // Map description
        trainingCourse.setDescription(rs.getString("description"));

        // Map LocalDate field from SQL Date for start date
        Date sqlStartDate = rs.getDate("start_date");
        if (sqlStartDate != null) {
            trainingCourse.setStartDate(sqlStartDate.toLocalDate());
        }

        // Map LocalDate field from SQL Date for end date
        Date sqlEndDate = rs.getDate("end_date");
        if (sqlEndDate != null) {
            trainingCourse.setEndDate(sqlEndDate.toLocalDate());
        }

        // Retrieve trainer id from ResultSet and fetch the full Employee using EmployeeDAO.
        int trainerId = rs.getInt("trainer_id"); // Assuming the column name is trainer_id
        if (trainerId > 0) { // Assuming trainer ID is positive
            Employee trainer = employeeDAO.findById(trainerId);
            trainingCourse.setTrainer(trainer);
        }

        return trainingCourse;
    }
}
