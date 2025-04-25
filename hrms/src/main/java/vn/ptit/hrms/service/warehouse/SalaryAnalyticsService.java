package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactSalaryDao;
import vn.ptit.hrms.dto.warehouse.*;

import java.util.List;

@Service
public class SalaryAnalyticsService {
    private final FactSalaryDao factSalaryDao;

    public SalaryAnalyticsService(FactSalaryDao factSalaryDao) {
        this.factSalaryDao = factSalaryDao;
    }

    /**
     * Get average salary statistics by department and position
     * 
     * @return List of salary statistics by department and position
     */
    public List<SalaryStatsByDeptPositionDTO> getSalaryStatsByDeptPosition() {
        return factSalaryDao.getSalaryStatsByDeptPosition();
    }

    /**
     * Get top employees with highest salaries
     * 
     * @param limit Number of employees to return
     * @return List of top salary employees
     */
    public List<TopSalaryEmployeeDTO> getTopEmployeesByHighestSalary(int limit) {
        return factSalaryDao.getTopEmployeesByHighestSalary(limit);
    }

    /**
     * Get top employees with lowest salaries
     * 
     * @param limit Number of employees to return
     * @return List of lowest salary employees
     */
    public List<TopSalaryEmployeeDTO> getTopEmployeesByLowestSalary(int limit) {
        return factSalaryDao.getTopEmployeesByLowestSalary(limit);
    }

    /**
     * Get salary ratio analysis by department
     * 
     * @return List of salary ratio by department
     */
    public List<SalaryRatioByDepartmentDTO> getSalaryRatioByDepartment() {
        return factSalaryDao.getSalaryRatioByDepartment();
    }

    /**
     * Get salary ratio analysis by employee
     * 
     * @return List of salary ratio by employee
     */
    public List<SalaryRatioByEmployeeDTO> getSalaryRatioByEmployee() {
        return factSalaryDao.getSalaryRatioByEmployee();
    }

    /**
     * Get salary trends by month for a specific year
     * 
     * @param year The year to analyze
     * @return List of salary trends by month
     */
    public List<SalaryTrendByMonthDTO> getSalaryTrendByMonth(int year) {
        return factSalaryDao.getSalaryTrendByMonth(year);
    }

    /**
     * Get salary analysis by age group and gender
     * 
     * @return List of salary statistics by age and gender
     */
    public List<SalaryByAgeGenderDTO> getSalaryByAgeGender() {
        return factSalaryDao.getSalaryByAgeGender();
    }
}