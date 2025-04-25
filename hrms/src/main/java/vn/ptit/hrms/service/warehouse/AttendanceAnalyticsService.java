package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactAttendanceDao;
import vn.ptit.hrms.dto.warehouse.DepartmentLateStatsDTO;
import vn.ptit.hrms.dto.warehouse.DepartmentPerformanceDTO;
import vn.ptit.hrms.dto.warehouse.EmployeeOvertimeDTO;
import vn.ptit.hrms.dto.warehouse.EmployeeWorkSummaryDTO;

import java.util.List;

@Service
public class AttendanceAnalyticsService {
    private final FactAttendanceDao factAttendanceDao;

    public AttendanceAnalyticsService(FactAttendanceDao factAttendanceDao) {
        this.factAttendanceDao = factAttendanceDao;
    }

    /**
     * Get total work hours of each employee in a specific year
     * 
     * @param year The year to analyze
     * @return List of employee work summaries
     */
    public List<EmployeeWorkSummaryDTO> getEmployeeWorkSummaryByYear(int year) {
        return factAttendanceDao.getEmployeeWorkSummaryByYear(year);
    }

    /**
     * Get departments with highest late rates
     * 
     * @param limit Number of departments to return
     * @return List of department late statistics
     */
    public List<DepartmentLateStatsDTO> getTopLateDepartments(int limit) {
        return factAttendanceDao.getTopLateDepartments(limit);
    }

    /**
     * Get employees with most overtime hours
     * 
     * @param limit Number of employees to return
     * @return List of employee overtime statistics
     */
    public List<EmployeeOvertimeDTO> getTopEmployeesByOvertime(int limit) {
        return factAttendanceDao.getTopEmployeesByOvertime(limit);
    }

    /**
     * Get performance statistics by department
     * 
     * @return List of department performance statistics
     */
    public List<DepartmentPerformanceDTO> getDepartmentPerformanceStats() {
        return factAttendanceDao.getDepartmentPerformanceStats();
    }
}