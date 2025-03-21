package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.DepartmentDao;

@Service
public class DepartmentService {
    private final DepartmentDao departmentDao;

    public DepartmentService(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
}
