package vn.ptit.hrms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import vn.ptit.hrms.constant.EmployeeStatusEnum;
import vn.ptit.hrms.constant.GenderEnum;
import vn.ptit.hrms.dao.PositionDao;
import vn.ptit.hrms.domain.Department;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.domain.Position;

public class EmployeeRowMapper implements RowMapper<Employee> {
    private final PositionDao positionDao;

    public EmployeeRowMapper(PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

      
        employee.setId(rs.getInt("EmployeeID"));

      
        employee.setPassword(rs.getNString("userPassword"));
        employee.setRoleName(rs.getNString("roleName"));

      
        employee.setFullName(rs.getNString("FullName"));

      
        Date sqlDateOfBirth = rs.getDate("DateOfBirth");
        if (sqlDateOfBirth != null) {
            LocalDate localDateOfBirth = sqlDateOfBirth.toLocalDate();
            employee.setDateOfBirth(localDateOfBirth);
        }

      
        String genderValue = rs.getNString("Gender");
        if (genderValue != null) {
            employee.setGender(getGenderEnum(genderValue));
        }

      
        employee.setAddress(rs.getNString("Address"));

      
        employee.setPhone(rs.getNString("Phone"));

      
        employee.setEmail(rs.getNString("Email"));

      
        int departmentId = rs.getInt("DepartmentID");
        if (departmentId > 0) {
            Department department = new Department();
            department.setId(departmentId);
            employee.setDepartment(department);
        }

      
        int positionId = rs.getInt("PositionID");
        if (positionId > 0) {
            Position position = positionDao.getPositionById(positionId);
            employee.setPosition(position);
        }

      
        Date sqlHireDate = rs.getDate("HireDate");
        if (sqlHireDate != null) {
            LocalDate localHireDate = sqlHireDate.toLocalDate();
            employee.setHireDate(localHireDate);
        }

      
        String statusValue = rs.getNString("Status");
        if (statusValue != null) {
            employee.setStatus(getEmployeeStatusEnum(statusValue));
        }

        return employee;
    }

    private GenderEnum getGenderEnum(String value) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getValue().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + value);
    }

    private EmployeeStatusEnum getEmployeeStatusEnum(String value) {
        for (EmployeeStatusEnum status : EmployeeStatusEnum.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown employee status value: " + value);
    }
}