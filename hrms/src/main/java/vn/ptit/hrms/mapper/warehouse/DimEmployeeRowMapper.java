package vn.ptit.hrms.mapper.warehouse;

import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.domain.warehouse.DimEmployee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DimEmployeeRowMapper implements RowMapper<DimEmployee> {

    @Override
    public DimEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
        DimEmployee employee = new DimEmployee();
        employee.setEmployeeSk(rs.getInt("employee_sk"));
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setFullName(rs.getString("full_name"));
        // Sử dụng getObject và convert sang LocalDate
        employee.setDateOfBirth(rs.getObject("date_of_birth", LocalDate.class));
        employee.setGender(rs.getString("gender").charAt(0));
        employee.setAddress(rs.getString("address"));
        employee.setPhone(rs.getString("phone"));
        employee.setEmail(rs.getString("email"));
        employee.setDepartmentSk(rs.getInt("department_sk"));
        employee.setDepartmentName(rs.getString("department_name"));
        employee.setPositionSk(rs.getInt("position_sk"));
        employee.setPositionName(rs.getString("position_name"));
        employee.setAge(rs.getInt("age"));
        // Sử dụng getObject và convert sang LocalDate
        employee.setHireDate(rs.getObject("hire_date", LocalDate.class));
        employee.setCurrentContractType(rs.getString("current_contract_type"));
        // Sử dụng getObject và convert sang LocalDate
        employee.setContractStartDate(rs.getObject("contract_start_date", LocalDate.class));
        // Sử dụng getObject và convert sang LocalDate
        employee.setContractEndDate(rs.getObject("contract_end_date", LocalDate.class));
        employee.setContractDuration(rs.getInt("contract_duration"));
        employee.setTotalYearsWorked(rs.getDouble("total_years_worked"));
        employee.setInsuranceNumber(rs.getString("insurance_number"));
        employee.setInsuranceType(rs.getString("insurance_type"));
        // Sử dụng getObject và convert sang LocalDate
        employee.setInsuranceStartDate(rs.getObject("insurance_start_date", LocalDate.class));
        // Sử dụng getObject và convert sang LocalDate
        employee.setInsuranceEndDate(rs.getObject("insurance_end_date", LocalDate.class));
        employee.setInsuranceDuration(rs.getInt("insurance_duration"));
        return employee;
    }
}

