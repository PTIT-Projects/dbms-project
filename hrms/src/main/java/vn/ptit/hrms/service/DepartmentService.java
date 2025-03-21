package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.DepartmentDao;
import vn.ptit.hrms.domain.Department;

import java.util.List;

@Service
public class DepartmentService {
    public final DepartmentDao departmentDao;

    public DepartmentService(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public void createDepartment(Department department) {
        departmentDao.createDepartment(department);
    }

    public Department getDepartmentById(Integer id) {
        return departmentDao.getDepartmentById(id);
    }

    public List<Department> getAllDepartments() {
        return departmentDao.getAllDepartments();
    }

    public void updateDepartment(Department department) {
        departmentDao.updateDepartment(department);
    }

    public void deleteDepartment(Integer id) {
        departmentDao.deleteDepartment(id);
    }
}