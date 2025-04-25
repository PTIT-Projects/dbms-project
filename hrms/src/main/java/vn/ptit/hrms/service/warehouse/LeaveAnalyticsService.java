package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactLeaveBalanceDao;
import vn.ptit.hrms.dto.warehouse.*;

import java.util.List;

@Service
public class LeaveAnalyticsService {
    private final FactLeaveBalanceDao factLeaveBalanceDao;

    public LeaveAnalyticsService(FactLeaveBalanceDao factLeaveBalanceDao) {
        this.factLeaveBalanceDao = factLeaveBalanceDao;
    }

    /**
     * Get leave statistics by department and position
     * 
     * @return List of leave statistics by department and position
     */
    public List<LeaveStatsByDeptPositionDTO> getLeaveStatsByDeptPosition() {
        return factLeaveBalanceDao.getLeaveStatsByDeptPosition();
    }

    /**
     * Get employees with low remaining leave days
     * 
     * @param maxRemainingDays Maximum number of remaining days to filter by
     * @return List of employees with low remaining leave
     */
    public List<LeaveUsageEmployeeDTO> getEmployeesWithLowRemainingLeave(int maxRemainingDays) {
        return factLeaveBalanceDao.getEmployeesWithLowRemainingLeave(maxRemainingDays);
    }

    /**
     * Get leave usage trends by month
     * 
     * @return List of leave usage by month
     */
    public List<LeaveTrendByMonthDTO> getLeaveTrendByMonth() {
        return factLeaveBalanceDao.getLeaveTrendByMonth();
    }

    /**
     * Get leave usage trends by quarter
     * 
     * @return List of leave usage by quarter
     */
    public List<LeaveTrendByQuarterDTO> getLeaveTrendByQuarter() {
        return factLeaveBalanceDao.getLeaveTrendByQuarter();
    }

    /**
     * Get statistics about leave type usage
     * 
     * @return List of leave type statistics
     */
    public List<LeaveTypeStatsDTO> getLeaveTypeStats() {
        return factLeaveBalanceDao.getLeaveTypeStats();
    }

    /**
     * Get leave usage analysis by employee experience level
     * 
     * @return List of leave usage by experience level
     */
    public List<LeaveByExperienceDTO> getLeaveByExperience() {
        return factLeaveBalanceDao.getLeaveByExperience();
    }
}