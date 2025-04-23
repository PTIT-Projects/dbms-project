package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.FactLeaveBalance;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactLeaveBalanceRowMapper implements RowMapper<FactLeaveBalance> {
    @Override
    public FactLeaveBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactLeaveBalance factLeaveBalance = new FactLeaveBalance();
        factLeaveBalance.setLeaveBalanceSk(rs.getInt("leave_balance_sk"));
        factLeaveBalance.setLeaveBalanceId(rs.getInt("leave_balance_id"));
        factLeaveBalance.setEmployeeSk(rs.getInt("employee_sk"));
        factLeaveBalance.setEmployeeName(rs.getNString("employee_name"));
        factLeaveBalance.setDepartmentName(rs.getNString("department_name"));
        factLeaveBalance.setPositionName(rs.getNString("position_name"));
        factLeaveBalance.setLeaveType(rs.getNString("leave_type"));
        factLeaveBalance.setDateSk(rs.getInt("date_sk"));
        factLeaveBalance.setGranularity(rs.getNString("granularity"));
        factLeaveBalance.setTotalLeaveDays(rs.getInt("total_leave_days"));
        factLeaveBalance.setUsedLeaveDays(rs.getInt("used_leave_days"));
        factLeaveBalance.setRemainingLeaveDays(rs.getInt("remaining_leave_days"));
        return factLeaveBalance;
    }
}
