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

   
    public void createTrainingCourse(TrainingCourse trainingCourse) {
        String sql = "INSERT INTO TrainingCourses (CourseName, Description, StartDate, EndDate, TrainerID) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                trainingCourse.getCourseName(),
                trainingCourse.getDescription(),
                trainingCourse.getStartDate(),
                trainingCourse.getEndDate(),
                trainingCourse.getTrainer() != null ? trainingCourse.getTrainer().getId() : null);
    }

   
    public TrainingCourse getTrainingCourseById(Integer id) {
        String sql = "SELECT * FROM TrainingCourses WHERE CourseID = ?";
        return jdbcTemplate.queryForObject(sql, trainingCourseRowMapper, id);
    }

   
    public List<TrainingCourse> getAllTrainingCourses() {
        String sql = "SELECT * FROM TrainingCourses";
        return jdbcTemplate.query(sql, trainingCourseRowMapper);
    }

   
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

   
    public void deleteTrainingCourse(Integer id) {
        String sql = "DELETE FROM TrainingCourses WHERE CourseID = ?";
        jdbcTemplate.update(sql, id);
    }
}