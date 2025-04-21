package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.primary.DepartmentManagerDao;
import vn.ptit.hrms.domain.primary.DepartmentManager;

import java.util.List;

@Service
public class DepartmentManagerService {
    public final DepartmentManagerDao departmentManagerDao;

    public DepartmentManagerService(DepartmentManagerDao departmentManagerDao) {
        this.departmentManagerDao = departmentManagerDao;
    }

    public void createDepartmentManager(DepartmentManager departmentManager) {
        departmentManagerDao.createDepartmentManager(departmentManager);
    }

    public DepartmentManager getDepartmentManagerByDepartmentId(Integer departmentId) {
        return departmentManagerDao.getDepartmentManagerByDepartmentId(departmentId);
    }

    public List<DepartmentManager> getAllDepartmentManagers() {
        return departmentManagerDao.getAllDepartmentManagers();
    }

    public void updateDepartmentManager(DepartmentManager departmentManager) {
        departmentManagerDao.updateDepartmentManager(departmentManager);
    }

    public void deleteDepartmentManager(Integer departmentId) {
        departmentManagerDao.deleteDepartmentManager(departmentId);
    }

    public boolean isDepartmentManager(Integer employeeId) {
        List<DepartmentManager> managers = departmentManagerDao.getAllDepartmentManagers();
        return managers.stream()
                .anyMatch(mgr -> mgr.getManager() != null &&
                        mgr.getManager().getId().equals(employeeId));
    }
}