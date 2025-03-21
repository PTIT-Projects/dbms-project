package vn.ptit.hrms.dao;

import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.WorkTripRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkTripRequestDao {
    private final Connection connection;

    public WorkTripRequestDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(WorkTripRequest request) throws SQLException {
        String sql = "INSERT INTO work_trip_request (employee_id, destination, start_date, end_date, purpose, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getEmployee().getId());
            stmt.setString(2, request.getDestination());
            stmt.setDate(3, Date.valueOf(request.getStartDate()));
            stmt.setDate(4, Date.valueOf(request.getEndDate()));
            stmt.setString(5, request.getPurpose());
            stmt.setString(6, request.getStatus().name());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    request.setId(rs.getInt(1));
                }
            }
        }
    }

    public WorkTripRequest findById(int id) throws SQLException {
        String sql = "SELECT * FROM work_trip_request WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWorkTripRequest(rs);
                }
            }
        }
        return null;
    }

    public List<WorkTripRequest> findAll() throws SQLException {
        List<WorkTripRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM work_trip_request";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToWorkTripRequest(rs));
            }
        }
        return list;
    }

    public void update(WorkTripRequest request) throws SQLException {
        String sql = "UPDATE work_trip_request SET employee_id = ?, destination = ?, start_date = ?, end_date = ?, purpose = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, request.getEmployee().getId());
            stmt.setString(2, request.getDestination());
            stmt.setDate(3, Date.valueOf(request.getStartDate()));
            stmt.setDate(4, Date.valueOf(request.getEndDate()));
            stmt.setString(5, request.getPurpose());
            stmt.setString(6, request.getStatus().name());
            stmt.setInt(7, request.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM work_trip_request WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private WorkTripRequest mapResultSetToWorkTripRequest(ResultSet rs) throws SQLException {
        WorkTripRequest request = new WorkTripRequest();
        request.setId(rs.getInt("id"));
        request.setEmployee(new Employee(rs.getInt("employee_id"))); // Giả định Employee có constructor Employee(int id)
        request.setDestination(rs.getString("destination"));
        request.setStartDate(rs.getDate("start_date").toLocalDate());
        request.setEndDate(rs.getDate("end_date").toLocalDate());
        request.setPurpose(rs.getString("purpose"));
        request.setStatus(RegistrationStatusEnum.valueOf(rs.getString("status")));
        return request;
    }
}

