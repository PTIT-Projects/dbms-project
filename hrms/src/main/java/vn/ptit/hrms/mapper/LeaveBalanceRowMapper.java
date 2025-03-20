package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.LeaveBalance;
import vn.ptit.hrms.dao.EmployeeDAO; // Assuming you have an EmployeeDAO to fetch Employee

public class LeaveBalanceRowMapper implements RowMapper<LeaveBalance> {

    private final EmployeeDAO employeeDAO;

    public LeaveBalanceRowMapper(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public LeaveBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
        LeaveBalance leaveBalance = new LeaveBalance();

        // Map LeaveBalance id
        leaveBalance.setId(rs.getInt("id"));

        // Retrieve employee id from ResultSet and fetch the full Employee using EmployeeDAO.
        int employeeId = rs.getInt("employee_id");
        Employee employee = employeeDAO.findById(employeeId);
        leaveBalance.setEmployee(employee);

        // Map year
        leaveBalance.setYear(rs.getInt("year"));

        // Map total leave days
        leaveBalance.setTotalLeaveDays(rs.getInt("total_leave_days"));

        // Map used leave days
        leaveBalance.setUsedLeaveDays(rs.getInt("used_leave_days"));

        // Map remaining leave days
        leaveBalance.setRemainingLeaveDays(rs.getInt("remaining_leave_days"));

        return leaveBalance;
    }
}
