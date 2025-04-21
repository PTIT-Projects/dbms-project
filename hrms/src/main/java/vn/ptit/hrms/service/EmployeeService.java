package vn.ptit.hrms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.EmployeeDao;
import vn.ptit.hrms.domain.Employee;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    public final EmployeeDao employeeDao;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeDao employeeDao, PasswordEncoder passwordEncoder) {
        this.employeeDao = employeeDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void createEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeDao.createEmployee(employee);
    }
    public Page<Employee> getEmployeesPage(Pageable pageable, String search, Integer departmentId, String status) {
        return employeeDao.findEmployeesPage(pageable, search, departmentId, status);
    }

    public Employee getEmployeeById(Integer id) {
        return employeeDao.getEmployeeById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    public void updateEmployee(Employee employee) {
        employeeDao.updateEmployee(employee);
    }

    public void deleteEmployee(Integer id) {
        employeeDao.deleteEmployee(id);
    }

    public boolean isExistsEmployeeWithEmail(String email) {
        return this.employeeDao.findEmployeeByEmail(email) != null;
    }

    public List<Employee> getEmployeesByDepartmentId(Integer departmentId) {
        return getAllEmployees().stream()
                .filter(emp -> emp.getDepartment() != null && 
                       emp.getDepartment().getId().equals(departmentId))
                .collect(Collectors.toList());
    }

    public int getEmployeeCount() {
        return employeeDao.countNumEmployees();
    }

}