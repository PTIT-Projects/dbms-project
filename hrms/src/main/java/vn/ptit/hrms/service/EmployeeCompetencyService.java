package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.EmployeeCompetencyDao;

@Service
public class EmployeeCompetencyService {
    private final EmployeeCompetencyDao employeeCompetencyDao;

    public EmployeeCompetencyService(EmployeeCompetencyDao employeeCompetencyDao) {
        this.employeeCompetencyDao = employeeCompetencyDao;
    }
}
