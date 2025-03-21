package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.EmployeeDao;

@Service
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
}
