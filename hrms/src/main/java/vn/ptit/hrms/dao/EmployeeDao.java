package vn.ptit.hrms.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.mapper.EmployeeRowMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDao {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper employeeRowMapper;

    public EmployeeDao(JdbcTemplate jdbcTemplate, EmployeeRowMapper employeeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeeRowMapper = employeeRowMapper;
    }
    public Page<Employee> findEmployeesPage(Pageable pageable, String search, Integer departmentId, String status) {
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM Employees e");
        StringBuilder dataSql = new StringBuilder("SELECT * FROM Employees e");
        List<Object> params = new ArrayList<>();

        // Add WHERE clause if needed
        if (departmentId != null || (search != null && !search.isEmpty()) || (status != null && !status.isEmpty())) {
            String whereClause = buildWhereClause(search, departmentId, status, params);
            countSql.append(whereClause);
            dataSql.append(whereClause);
        }

        // Count total records
        Integer total = jdbcTemplate.queryForObject(countSql.toString(), Integer.class, params.toArray());

        // Add pagination to data query
        dataSql.append(" ORDER BY e.EmployeeID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(pageable.getOffset());
        params.add(pageable.getPageSize());
        System.out.println(dataSql);
        // Execute query
        List<Employee> employees = jdbcTemplate.query(dataSql.toString(), employeeRowMapper, params.toArray());

        return new PageImpl<>(employees, pageable, total != null ? total : 0);
    }
    private String buildWhereClause(String search, Integer departmentId, String status, List<Object> params) {
        StringBuilder whereClause = new StringBuilder(" WHERE ");
        boolean hasCondition = false;

        if (search != null && !search.isEmpty()) {
            whereClause.append("(e.FullName LIKE ? OR e.Email LIKE ? OR e.Phone LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
            hasCondition = true;
        }

        if (departmentId != null) {
            if (hasCondition) {
                whereClause.append(" AND ");
            }
            whereClause.append("e.DepartmentID = ?");
            params.add(departmentId);
            hasCondition = true;
        }

        if (status != null && !status.isEmpty()) {
            if (hasCondition) {
                whereClause.append(" AND ");
            }
            whereClause.append("e.Status = ?");
            params.add(status);
        }

        return whereClause.toString();
    }

    public void createEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (userPassword, roleName, FullName, DateOfBirth, Gender, Address, Phone, Email, DepartmentID, PositionID, HireDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                employee.getPassword(),
                employee.getRoleName(),
                employee.getFullName(),
                employee.getDateOfBirth(),
                employee.getGender() != null ? employee.getGender().getValue() : null,
                employee.getAddress(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getPosition().getId(),
                employee.getHireDate(),
                employee.getStatus() != null ? employee.getStatus().getValue() : null);
    }

    public Employee getEmployeeById(Integer id) {
        String sql = "SELECT * FROM Employees WHERE EmployeeID = ?";
        return jdbcTemplate.queryForObject(sql, employeeRowMapper, id);
    }

    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM Employees";
        return jdbcTemplate.query(sql, employeeRowMapper);
    }

    public void updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET userPassword = ?, roleName = ?, FullName = ?, DateOfBirth = ?, Gender = ?, Address = ?, Phone = ?, Email = ?, DepartmentID = ?, PositionID = ?, HireDate = ?, Status = ? WHERE EmployeeID = ?";
        jdbcTemplate.update(sql,
                employee.getPassword(),
                employee.getRoleName(),
                employee.getFullName(),
                employee.getDateOfBirth(),
                employee.getGender() != null ? employee.getGender().getValue() : null,
                employee.getAddress(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getPosition().getId(),
                employee.getHireDate(),
                employee.getStatus() != null ? employee.getStatus().getValue() : null,
                employee.getId());
    }

    public void deleteEmployee(Integer id) {
        String sql = "DELETE FROM Employees WHERE EmployeeID = ?";
        jdbcTemplate.update(sql, id);
    }
    public Employee findEmployeeByEmail(String email) {
        String sql = "SELECT * FROM Employees WHERE Email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, employeeRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public int countNumEmployees() {
        String sql = "SELECT COUNT(*) FROM Employees";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return (count != null) ? count : 0;
    }
}