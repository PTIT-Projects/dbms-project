package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.LeaveBalance;
import vn.ptit.hrms.dao.EmployeeDao;

public class LeaveBalanceRowMapper implements RowMapper<LeaveBalance> {

    private final EmployeeDao employeeDAO;

    public LeaveBalanceRowMapper(EmployeeDao employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public LeaveBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
        LeaveBalance leaveBalance = new LeaveBalance();

       
        leaveBalance.setId(rs.getInt("LeaveBalanceID"));

       
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        leaveBalance.setEmployee(employee);

       
        leaveBalance.setYear(rs.getInt("Year"));

       
        leaveBalance.setTotalLeaveDays(rs.getInt("TotalLeaveDays"));

       
        leaveBalance.setUsedLeaveDays(rs.getInt("UsedLeaveDays"));

       
        leaveBalance.setRemainingLeaveDays(rs.getInt("RemainingLeaveDays"));

        return leaveBalance;
    }
}