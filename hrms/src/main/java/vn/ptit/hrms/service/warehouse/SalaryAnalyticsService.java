package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactSalaryDao;
import vn.ptit.hrms.dto.warehouse.YearlyAverageSalaryDTO;
import vn.ptit.hrms.dto.warehouse.SalaryGrowthRateDTO;
import vn.ptit.hrms.dto.warehouse.*;

import java.util.List;

@Service
public class SalaryAnalyticsService {
    private final FactSalaryDao factSalaryDao;

    public SalaryAnalyticsService(FactSalaryDao factSalaryDao) {
        this.factSalaryDao = factSalaryDao;
    }

    public List<YearlyAverageSalaryDTO> getAverageSalaryByYear() {
        return factSalaryDao.getAverageSalaryByYear();
    }

    public List<SalaryGrowthRateDTO> getSalaryGrowthRate() {
        return factSalaryDao.getSalaryGrowthRate();
    }


    public List<SalaryStatsByDeptPositionDTO> getSalaryStatsByDeptPosition() {
        return factSalaryDao.getSalaryStatsByDeptPosition();
    }


    public List<TopSalaryEmployeeDTO> getTopEmployeesByHighestSalary(int limit) {
        return factSalaryDao.getTopEmployeesByHighestSalary(limit);
    }
    public List<TopSalaryEmployeeDTO> getTopEmployeesByLowestSalary(int limit) {
        return factSalaryDao.getTopEmployeesByLowestSalary(limit);
    }

    public List<SalaryRatioByDepartmentDTO> getSalaryRatioByDepartment() {
        return factSalaryDao.getSalaryRatioByDepartment();
    }

    public List<SalaryRatioByEmployeeDTO> getSalaryRatioByEmployee() {
        return factSalaryDao.getSalaryRatioByEmployee();
    }

    public List<SalaryTrendByMonthDTO> getSalaryTrendByMonth(int year) {
        return factSalaryDao.getSalaryTrendByMonth(year);
    }

    public List<SalaryByAgeGenderDTO> getSalaryByAgeGender() {
        return factSalaryDao.getSalaryByAgeGender();
    }
}