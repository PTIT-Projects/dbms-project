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

        // Map LeaveBalance id using the correct column name
        leaveBalance.setId(rs.getInt("LeaveBalanceID"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO
        int employeeId = rs.getInt("EmployeeID");
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        leaveBalance.setEmployee(employee);

        // Map year
        leaveBalance.setYear(rs.getInt("Year"));

        // Map total leave days
        leaveBalance.setTotalLeaveDays(rs.getInt("TotalLeaveDays"));

        // Map used leave days
        leaveBalance.setUsedLeaveDays(rs.getInt("UsedLeaveDays"));

        // Map remaining leave days
        leaveBalance.setRemainingLeaveDays(rs.getInt("RemainingLeaveDays"));

        return leaveBalance;
    }
}