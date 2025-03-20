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
        String sql = "INSERT INTO leave_balances (employee_id, year, total_leave_days, used_leave_days, remaining_leave_days) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                leaveBalance.getEmployee() != null ? leaveBalance.getEmployee().getId() : null,
                leaveBalance.getYear(),
                leaveBalance.getTotalLeaveDays(),
                leaveBalance.getUsedLeaveDays(),
                leaveBalance.getRemainingLeaveDays());
    }

    // Method to get a leave balance record by ID
    public LeaveBalance getLeaveBalanceById(Integer id) {
        String sql = "SELECT * FROM leave_balances WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, leaveBalanceRowMapper, id);
    }

    // Method to get all leave balance records
    public List<LeaveBalance> getAllLeaveBalances() {
        String sql = "SELECT * FROM leave_balances";
        return jdbcTemplate.query(sql, leaveBalanceRowMapper);
    }

    // Method to update a leave balance record
    public void updateLeaveBalance(LeaveBalance leaveBalance) {
        String sql = "UPDATE leave_balances SET employee_id = ?, year = ?, total_leave_days = ?, used_leave_days = ?, remaining_leave_days = ? WHERE id = ?";
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
        String sql = "DELETE FROM leave_balances WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
