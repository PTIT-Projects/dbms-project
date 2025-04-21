package vn.ptit.hrms.dao.primary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.WorkTripRequest;
import vn.ptit.hrms.mapper.WorkTripRequestRowMapper;

import java.util.List;

@Repository
public class WorkTripRequestDao {
    private final JdbcTemplate jdbcTemplate;
    private final WorkTripRequestRowMapper workTripRequestRowMapper;

    public WorkTripRequestDao(JdbcTemplate jdbcTemplate, WorkTripRequestRowMapper workTripRequestRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.workTripRequestRowMapper = workTripRequestRowMapper;
    }

   
    public void createWorkTripRequest(WorkTripRequest workTripRequest) {
        String sql = "INSERT INTO WorkTripRequests (EmployeeID, Destination, StartDate, EndDate, Purpose, Status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                workTripRequest.getEmployee() != null ? workTripRequest.getEmployee().getId() : null,
                workTripRequest.getDestination(),
                workTripRequest.getStartDate(),
                workTripRequest.getEndDate(),
                workTripRequest.getPurpose(),
                workTripRequest.getStatus() != null ? workTripRequest.getStatus().getValue() : null);
    }

   
    public WorkTripRequest getWorkTripRequestById(Integer id) {
        String sql = "SELECT * FROM WorkTripRequests WHERE RequestID = ?";
        return jdbcTemplate.queryForObject(sql, workTripRequestRowMapper, id);
    }

   
    public List<WorkTripRequest> getAllWorkTripRequests() {
        String sql = "SELECT * FROM WorkTripRequests";
        return jdbcTemplate.query(sql, workTripRequestRowMapper);
    }

   
    public void updateWorkTripRequest(WorkTripRequest workTripRequest) {
        String sql = "UPDATE WorkTripRequests SET EmployeeID = ?, Destination = ?, StartDate = ?, EndDate = ?, Purpose = ?, Status = ? WHERE RequestID = ?";
        jdbcTemplate.update(sql,
                workTripRequest.getEmployee() != null ? workTripRequest.getEmployee().getId() : null,
                workTripRequest.getDestination(),
                workTripRequest.getStartDate(),
                workTripRequest.getEndDate(),
                workTripRequest.getPurpose(),
                workTripRequest.getStatus() != null ? workTripRequest.getStatus().getValue() : null,
                workTripRequest.getId());
    }

   
    public void deleteWorkTripRequest(Integer id) {
        String sql = "DELETE FROM WorkTripRequests WHERE RequestID = ?";
        jdbcTemplate.update(sql, id);
    }
}