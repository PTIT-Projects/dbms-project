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
        String sql = "INSERT INTO training_courses (course_name, description, start_date, end_date, trainer_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                trainingCourse.getCourseName(),
                trainingCourse.getDescription(),
                trainingCourse.getStartDate() != null ? java.sql.Date.valueOf(trainingCourse.getStartDate()) : null,
                trainingCourse.getEndDate() != null ? java.sql.Date.valueOf(trainingCourse.getEndDate()) : null,
                trainingCourse.getTrainer() != null ? trainingCourse.getTrainer().getId() : null);
    }

    // Method to get a training course by ID
    public TrainingCourse getTrainingCourseById(Integer id) {
        String sql = "SELECT * FROM training_courses WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, trainingCourseRowMapper, id);
    }

    // Method to get all training courses
    public List<TrainingCourse> getAllTrainingCourses() {
        String sql = "SELECT * FROM training_courses";
        return jdbcTemplate.query(sql, trainingCourseRowMapper);
    }

    // Method to update a training course
    public void updateTrainingCourse(TrainingCourse trainingCourse) {
        String sql = "UPDATE training_courses SET course_name = ?, description = ?, start_date = ?, end_date = ?, trainer_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                trainingCourse.getCourseName(),
                trainingCourse.getDescription(),
                trainingCourse.getStartDate() != null ? java.sql.Date.valueOf(trainingCourse.getStartDate()) : null,
                trainingCourse.getEndDate() != null ? java.sql.Date.valueOf(trainingCourse.getEndDate()) : null,
                trainingCourse.getTrainer() != null ? trainingCourse.getTrainer().getId() : null,
                trainingCourse.getId());
    }

    // Method to delete a training course
    public void deleteTrainingCourse(Integer id) {
        String sql = "DELETE FROM training_courses WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
