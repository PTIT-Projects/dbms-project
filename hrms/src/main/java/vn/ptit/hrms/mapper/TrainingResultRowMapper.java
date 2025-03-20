package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.TrainingCourse;
import vn.ptit.hrms.domain.TrainingResult;
import vn.ptit.hrms.constant.TrainingCompletionStatusEnum;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee
import vn.ptit.hrms.dao.TrainingCourseDAO; // Assuming you have a TrainingCourseDAO to fetch TrainingCourse

public class TrainingResultRowMapper implements RowMapper<TrainingResult> {

    private final EmployeeDAO employeeDAO;
    private final TrainingCourseDAO trainingCourseDAO;

    public TrainingResultRowMapper(EmployeeDAO employeeDAO, TrainingCourseDAO trainingCourseDAO) {
        this.employeeDAO = employeeDAO;
        this.trainingCourseDAO = trainingCourseDAO;
    }

    @Override
    public TrainingResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        TrainingResult trainingResult = new TrainingResult();

        // Map TrainingResult id
        trainingResult.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        if (employeeId > 0) { // Assuming employee ID is positive
            Employee employee = employeeDAO.findById(employeeId);
            trainingResult.setEmployee(employee);
        }

        // Retrieve course id from ResultSet and fetch the full TrainingCourse using TrainingCourseDAO.
        int courseId = rs.getInt("course_id"); // Assuming the column name is course_id
        if (courseId > 0) { // Assuming course ID is positive
            TrainingCourse course = trainingCourseDAO.findById(courseId);
            trainingResult.setCourse(course);
        }

        // Map TrainingCompletionStatusEnum field
        String completionStatusValue = rs.getString("completion_status");
        if (completionStatusValue != null) {
            trainingResult.setCompletionStatus(getTrainingCompletionStatusEnum(completionStatusValue));
        }

        // Map score
        trainingResult.setScore(rs.getDouble("score"));

        return trainingResult;
    }

    /**
     * Converts a string value from the database into the corresponding TrainingCompletionStatusEnum.
     */
    private TrainingCompletionStatusEnum getTrainingCompletionStatusEnum(String value) {
        for (TrainingCompletionStatusEnum status : TrainingCompletionStatusEnum.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown training completion status value: " + value);
    }
}
