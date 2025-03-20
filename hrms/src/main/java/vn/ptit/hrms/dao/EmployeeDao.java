package vn.ptit.hrms.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.mapper.EmployeeRowMapper;

import java.util.List;

@Repository
public class EmployeeDao {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper employeeRowMapper;

    public EmployeeDao(JdbcTemplate jdbcTemplate, EmployeeRowMapper employeeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeeRowMapper = employeeRowMapper;
    }

    // Method to create a new employee
    public void createEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (FullName, DateOfBirth, Gender, Address, Phone, Email, DepartmentID, Position, HireDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                employee.getFullName(),
                employee.getDateOfBirth(),
                employee.getGender() != null ? employee.getGender().getValue() : null,
                employee.getAddress(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getPosition(),
                employee.getHireDate(),
                employee.getStatus() != null ? employee.getStatus().getValue() : null);
    }

    // Method to get an employee by ID
    public Employee getEmployeeById(Integer id) {
        String sql = "SELECT * FROM Employees WHERE EmployeeID = ?";
        return jdbcTemplate.queryForObject(sql, employeeRowMapper, id);
    }

    // Method to get all employees
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM Employees";
        return jdbcTemplate.query(sql, employeeRowMapper);
    }

    // Method to update an employee
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET FullName = ?, DateOfBirth = ?, Gender = ?, Address = ?, Phone = ?, Email = ?, DepartmentID = ?, Position = ?, HireDate = ?, Status = ? WHERE EmployeeID = ?";
        jdbcTemplate.update(sql,
                employee.getFullName(),
                employee.getDateOfBirth(),
                employee.getGender() != null ? employee.getGender().getValue() : null,
                employee.getAddress(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getPosition(),
                employee.getHireDate(),
                employee.getStatus() != null ? employee.getStatus().getValue() : null,
                employee.getId());
    }

    // Method to delete an employee
    public void deleteEmployee(Integer id) {
        String sql = "DELETE FROM Employees WHERE EmployeeID = ?";
        jdbcTemplate.update(sql, id);
    }
}