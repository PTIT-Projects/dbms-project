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
        employee.setId(rs.getInt("EmployeeID"));

        // Map Employee full name
        employee.setFullName(rs.getString("FullName"));

        // Map LocalDate field from SQL Date for date of birth
        Date sqlDateOfBirth = rs.getDate("DateOfBirth");
        if (sqlDateOfBirth != null) {
            LocalDate localDateOfBirth = sqlDateOfBirth.toLocalDate();
            employee.setDateOfBirth(localDateOfBirth);
        }

        // Map GenderEnum field
        String genderValue = rs.getString("Gender");
        if (genderValue != null) {
            employee.setGender(getGenderEnum(genderValue));
        }

        // Map Employee address
        employee.setAddress(rs.getString("Address"));

        // Map Employee phone
        employee.setPhone(rs.getString("Phone"));

        // Map Employee email
        employee.setEmail(rs.getString("Email"));

        // Map Department (using departmentId only, actual Department object should be set by DAO)
        int departmentId = rs.getInt("DepartmentID");
        if (departmentId > 0) {
            Department department = new Department();
            department.setId(departmentId);
            employee.setDepartment(department);
        }

        // Map Employee position
        employee.setPosition(rs.getString("Position"));

        // Map LocalDate field from SQL Date for hire date
        Date sqlHireDate = rs.getDate("HireDate");
        if (sqlHireDate != null) {
            LocalDate localHireDate = sqlHireDate.toLocalDate();
            employee.setHireDate(localHireDate);
        }

        // Map EmployeeStatusEnum field
        String statusValue = rs.getString("Status");
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
            if (gender.getValue().equalsIgnoreCase(value)) {
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
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown employee status value: " + value);
    }
}