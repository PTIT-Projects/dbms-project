package vn.ptit.hrms.dao.primary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.primary.LeaveBalance;
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

   
    public void createLeaveBalance(LeaveBalance leaveBalance) {
        String sql = "INSERT INTO LeaveBalances (EmployeeID, Year, TotalLeaveDays, UsedLeaveDays, RemainingLeaveDays) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                leaveBalance.getEmployee() != null ? leaveBalance.getEmployee().getId() : null,
                leaveBalance.getYear(),
                leaveBalance.getTotalLeaveDays(),
                leaveBalance.getUsedLeaveDays(),
                leaveBalance.getRemainingLeaveDays());
    }

   
    public LeaveBalance getLeaveBalanceById(Integer id) {
        String sql = "SELECT * FROM LeaveBalances WHERE LeaveBalanceID = ?";
        return jdbcTemplate.queryForObject(sql, leaveBalanceRowMapper, id);
    }

   
    public List<LeaveBalance> getAllLeaveBalances() {
        String sql = "SELECT * FROM LeaveBalances";
        return jdbcTemplate.query(sql, leaveBalanceRowMapper);
    }

   
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

   
    public void deleteLeaveBalance(Integer id) {
        String sql = "DELETE FROM LeaveBalances WHERE LeaveBalanceID = ?";
        jdbcTemplate.update(sql, id);
    }
}