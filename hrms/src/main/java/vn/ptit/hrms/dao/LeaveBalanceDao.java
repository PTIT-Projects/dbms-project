package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.LeaveBalance;
import vn.ptit.hrms.mapper.LeaveBalanceRowMapper;

import java.util.List;

@Repository
public class LeaveBalanceDao {
    private final JdbcTemplate jdbcTemplate;
    private final LeaveBalanceRowMapper leaveBalanceRowMapper;

    public LeaveBalanceDao(JdbcTemplate jdbcTemplate, LeaveBalanceRowMapper leaveBalanceRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.leaveBalanceRowMapper = leaveBalanceRowMapper;
    }

    // Method to create a new leave balance record
    public void createLeaveBalance(LeaveBalance leaveBalance) {
        String sql = "INSERT INTO LeaveBalances (EmployeeID, Year, TotalLeaveDays, UsedLeaveDays, RemainingLeaveDays) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                leaveBalance.getEmployee() != null ? leaveBalance.getEmployee().getId() : null,
                leaveBalance.getYear(),
                leaveBalance.getTotalLeaveDays(),
                leaveBalance.getUsedLeaveDays(),
                leaveBalance.getRemainingLeaveDays());
    }

    // Method to get a leave balance record by ID
    public LeaveBalance getLeaveBalanceById(Integer id) {
        String sql = "SELECT * FROM LeaveBalances WHERE LeaveBalanceID = ?";
        return jdbcTemplate.queryForObject(sql, leaveBalanceRowMapper, id);
    }

    // Method to get all leave balance records
    public List<LeaveBalance> getAllLeaveBalances() {
        String sql = "SELECT * FROM LeaveBalances";
        return jdbcTemplate.query(sql, leaveBalanceRowMapper);
    }

    // Method to update a leave balance record
    public void updateLeaveBalance(LeaveBalance leaveBalance) {
        String sql = "UPDATE LeaveBalances SET EmployeeID = ?, Year = ?, TotalLeaveDays = ?, UsedLeaveDays = ?, RemainingLeaveDays = ? WHERE LeaveBalanceID = ?";
        jdbcTemplate.update(sql,
                leaveBalance.getEmployee() != null ? leaveBalance.getEmployee().getId() : null,
                leaveBalance.getYear(),
                leaveBalance.getTotalLeaveDays(),
                leaveBalance.getUsedLeaveDays(),
                leaveBalance.getRemainingLeaveDays(),
                leaveBalance.getId());
    }

    // Method to delete a leave balance record
    public void deleteLeaveBalance(Integer id) {
        String sql = "DELETE FROM LeaveBalances WHERE LeaveBalanceID = ?";
        jdbcTemplate.update(sql, id);
    }
}