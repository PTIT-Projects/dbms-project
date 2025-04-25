package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.DimEmployeeDao;
import vn.ptit.hrms.domain.warehouse.DimEmployee;

import java.util.List;

@Service
public class DimEmployeeService {

    private final DimEmployeeDao dimEmployeeDao;

    public DimEmployeeService(DimEmployeeDao dimEmployeeDao) {
        this.dimEmployeeDao = dimEmployeeDao;
    }

    public List<DimEmployee> getAllDimEmployees() {
        return dimEmployeeDao.getEmployees();
    }

    public List<DimEmployee> getDimEmployeesByDepartmentSk(Integer departmentSk) {
        return dimEmployeeDao.getEmployeesByDepartment(departmentSk);
    }

    public List<DimEmployee> getDimEmployeesByDepartmentName(String departmentName) {
        return dimEmployeeDao.getEmployeesByDepartmentName(departmentName);
    }

    public List<DimEmployee> getDimEmployeesByTotalYearsWorked(double totalYearsWorked) {
        return dimEmployeeDao.getEmployeesByTotalYearsWorked(totalYearsWorked);
    }

    public List<DimEmployee> getDimEmployeesByPositionSk(Integer positionSk) {
        return dimEmployeeDao.getEmployeesByPositionSk(positionSk);
    }

    public List<DimEmployee> getDimEmployeesByPositionName(String positionName) {
        return dimEmployeeDao.getEmployeesByPositionName(positionName);
    }
}
