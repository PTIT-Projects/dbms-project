package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.TrainingResult;
import vn.ptit.hrms.mapper.TrainingResultRowMapper;

import java.util.List;

@Repository
public class TrainingResultDao {
    private final JdbcTemplate jdbcTemplate;
    private final TrainingResultRowMapper trainingResultRowMapper;

    public TrainingResultDao(JdbcTemplate jdbcTemplate, TrainingResultRowMapper trainingResultRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.trainingResultRowMapper = trainingResultRowMapper;
    }

    // Method to create a new training result record
    public void createTrainingResult(TrainingResult trainingResult) {
        String sql = "INSERT INTO training_results (employee_id, course_id, completion_status, score) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                trainingResult.getEmployee() != null ? trainingResult.getEmployee().getId() : null,
                trainingResult.getCourse() != null ? trainingResult.getCourse().getId() : null,
                trainingResult.getCompletionStatus() != null ? trainingResult.getCompletionStatus().name() : null,
                trainingResult.getScore());
    }

    // Method to get a training result record by ID
    public TrainingResult getTrainingResultById(Integer id) {
        String sql = "SELECT * FROM training_results WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, trainingResultRowMapper, id);
    }

    // Method to get all training result records
    public List<TrainingResult> getAllTrainingResults() {
        String sql = "SELECT * FROM training_results";
        return jdbcTemplate.query(sql, trainingResultRowMapper);
    }

    // Method to update a training result record
    public void updateTrainingResult(TrainingResult trainingResult) {
        String sql = "UPDATE training_results SET employee_id = ?, course_id = ?, completion_status = ?, score = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                trainingResult.getEmployee() != null ? trainingResult.getEmployee().getId() : null,
                trainingResult.getCourse() != null ? trainingResult.getCourse().getId() : null,
                trainingResult.getCompletionStatus() != null ? trainingResult.getCompletionStatus().name() : null,
                trainingResult.getScore(),
                trainingResult.getId());
    }

    // Method to delete a training result record
    public void deleteTrainingResult(Integer id) {
        String sql = "DELETE FROM training_results WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
