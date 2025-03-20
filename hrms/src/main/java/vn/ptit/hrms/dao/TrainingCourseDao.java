package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.TrainingCourse;
import vn.ptit.hrms.mapper.TrainingCourseRowMapper;

import java.util.List;

@Repository
public class TrainingCourseDao {
    private final JdbcTemplate jdbcTemplate;
    private final TrainingCourseRowMapper trainingCourseRowMapper;

    public TrainingCourseDao(JdbcTemplate jdbcTemplate, TrainingCourseRowMapper trainingCourseRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.trainingCourseRowMapper = trainingCourseRowMapper;
    }

    // Method to create a new training course
    public void createTrainingCourse(TrainingCourse trainingCourse) {
        String sql = "INSERT INTO TrainingCourses (CourseName, Description, StartDate, EndDate, TrainerID) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                trainingCourse.getCourseName(),
                trainingCourse.getDescription(),
                trainingCourse.getStartDate(),
                trainingCourse.getEndDate(),
                trainingCourse.getTrainer() != null ? trainingCourse.getTrainer().getId() : null);
    }

    // Method to get a training course by ID
    public TrainingCourse getTrainingCourseById(Integer id) {
        String sql = "SELECT * FROM TrainingCourses WHERE CourseID = ?";
        return jdbcTemplate.queryForObject(sql, trainingCourseRowMapper, id);
    }

    // Method to get all training courses
    public List<TrainingCourse> getAllTrainingCourses() {
        String sql = "SELECT * FROM TrainingCourses";
        return jdbcTemplate.query(sql, trainingCourseRowMapper);
    }

    // Method to update a training course
    public void updateTrainingCourse(TrainingCourse trainingCourse) {
        String sql = "UPDATE TrainingCourses SET CourseName = ?, Description = ?, StartDate = ?, EndDate = ?, TrainerID = ? WHERE CourseID = ?";
        jdbcTemplate.update(sql,
                trainingCourse.getCourseName(),
                trainingCourse.getDescription(),
                trainingCourse.getStartDate(),
                trainingCourse.getEndDate(),
                trainingCourse.getTrainer() != null ? trainingCourse.getTrainer().getId() : null,
                trainingCourse.getId());
    }

    // Method to delete a training course
    public void deleteTrainingCourse(Integer id) {
        String sql = "DELETE FROM TrainingCourses WHERE CourseID = ?";
        jdbcTemplate.update(sql, id);
    }
}