package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.TrainingCourse;
import vn.ptit.hrms.domain.TrainingResult;
import vn.ptit.hrms.constant.TrainingCompletionStatusEnum;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.dao.TrainingCourseDao;

public class TrainingResultRowMapper implements RowMapper<TrainingResult> {

    private final EmployeeDao employeeDAO;
    private final TrainingCourseDao trainingCourseDAO;

    public TrainingResultRowMapper(EmployeeDao employeeDAO, TrainingCourseDao trainingCourseDAO) {
        this.employeeDAO = employeeDAO;
        this.trainingCourseDAO = trainingCourseDAO;
    }

    @Override
    public TrainingResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        TrainingResult trainingResult = new TrainingResult();

        // Map TrainingResult id
        trainingResult.setId(rs.getInt("ResultID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO
        int employeeId = rs.getInt("EmployeeID");
        if (employeeId > 0) {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            trainingResult.setEmployee(employee);
        }

        // Retrieve course id from ResultSet and fetch the full TrainingCourse using TrainingCourseDAO
        int courseId = rs.getInt("CourseID");
        if (courseId > 0) {
            TrainingCourse course = trainingCourseDAO.getTrainingCourseById(courseId);
            trainingResult.setCourse(course);
        }

        // Map TrainingCompletionStatusEnum field
        String completionStatusValue = rs.getString("CompletionStatus");
        if (completionStatusValue != null) {
            trainingResult.setCompletionStatus(getTrainingCompletionStatusEnum(completionStatusValue));
        }

        // Map score
        trainingResult.setScore(rs.getDouble("Score"));

        return trainingResult;
    }

    /**
     * Converts a string value from the database into the corresponding TrainingCompletionStatusEnum.
     */
    private TrainingCompletionStatusEnum getTrainingCompletionStatusEnum(String value) {
        for (TrainingCompletionStatusEnum status : TrainingCompletionStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) { // Changed from name() to getValue()
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown training completion status value: " + value);
    }
}