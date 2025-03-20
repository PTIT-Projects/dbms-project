package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.EmployeeStatusEnum;
import vn.ptit.hrms.constant.GenderEnum;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.Employee;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        // Map Employee id
        employee.setId(rs.getInt("id"));

        // Map Employee full name
        employee.setFullName(rs.getString("full_name"));

        // Map LocalDate field from SQL Date for date of birth
        Date sqlDateOfBirth = rs.getDate("date_of_birth");
        if (sqlDateOfBirth != null) {
            LocalDate localDateOfBirth = sqlDateOfBirth.toLocalDate();
            employee.setDateOfBirth(localDateOfBirth);
        }

        // Map GenderEnum field
        String genderValue = rs.getString("gender");
        if (genderValue != null) {
            employee.setGender(getGenderEnum(genderValue));
        }

        // Map Employee address
        employee.setAddress(rs.getString("address"));

        // Map Employee phone
        employee.setPhone(rs.getString("phone"));

        // Map Employee email
        employee.setEmail(rs.getString("email"));

        // Map Department (assuming you have a method to fetch Department by ID)
        int departmentId = rs.getInt("department_id");
        Department department = fetchDepartmentById(departmentId); // Implement this method as needed
        employee.setDepartment(department);

        // Map Employee position
        employee.setPosition(rs.getString("position"));

        // Map LocalDate field from SQL Date for hire date
        Date sqlHireDate = rs.getDate("hire_date");
        if (sqlHireDate != null) {
            LocalDate localHireDate = sqlHireDate.toLocalDate();
            employee.setHireDate(localHireDate);
        }

        // Map EmployeeStatusEnum field
        String statusValue = rs.getString("status");
        if (statusValue != null) {
            employee.setStatus(getEmployeeStatusEnum(statusValue));
        }

        return employee;
    }

    /**
     * Converts a string value from the database into the corresponding GenderEnum.
     */
    private GenderEnum getGenderEnum(String value) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + value);
    }

    /**
     * Converts a string value from the database into the corresponding EmployeeStatusEnum.
     */
    private EmployeeStatusEnum getEmployeeStatusEnum(String value) {
        for (EmployeeStatusEnum status : EmployeeStatusEnum.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown employee status value: " + value);
    }

    // Placeholder for fetching Department by ID
    private Department fetchDepartmentById(int departmentId) {
        // Implement the logic to fetch the Department based on the departmentId
        // This could involve calling a DAO method or similar
        return null; // Replace with actual implementation
    }
}
