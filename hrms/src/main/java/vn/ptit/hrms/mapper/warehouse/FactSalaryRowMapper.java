package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.FactSalary;
import vn.ptit.hrms.constant.FactSalaryEnum; // Import the enum

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactSalaryRowMapper implements RowMapper<FactSalary> {

    @Override
    public FactSalary mapRow(ResultSet rs, int rowNum) throws SQLException {
        FactSalary salary = new FactSalary();
        salary.setSalarySk(rs.getInt("salary_sk"));
        salary.setSalaryId(rs.getInt("salary_id"));
        salary.setEmployeeSk(rs.getInt("employee_sk"));
        salary.setEmployeeName(rs.getString("employee_name"));
        salary.setDepartmentName(rs.getString("department_name"));
        salary.setPositionName(rs.getString("position_name"));
        salary.setDateSk(rs.getInt("date_sk"));
        salary.setBasicSalary(rs.getDouble("basic_salary"));
        salary.setAllowance(rs.getDouble("allowance"));
        salary.setDeductions(rs.getDouble("deductions"));
        salary.setTotalSalary(rs.getDouble("total_salary"));

        // Convert from String to Enum
        String paymentStatusString = rs.getString("payment_status");
        FactSalaryEnum paymentStatus = null;
        if (paymentStatusString != null) {
            try {
                paymentStatus = FactSalaryEnum.valueOf(paymentStatusString);
            } catch (IllegalArgumentException e) {
                // Handle the case where the string doesn't match any enum value
                // You might want to log an error or set a default value
                paymentStatus = FactSalaryEnum.NOTPAID; // Or set a default enum value
            }
        }
        salary.setPaymentStatus(paymentStatus);
        return salary;
    }
}
