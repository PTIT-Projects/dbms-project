package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactWorkTripDao;
import vn.ptit.hrms.dto.warehouse.*;

import java.util.List;

@Service
public class WorkTripAnalyticsService {
    private final FactWorkTripDao factWorkTripDao;

    public WorkTripAnalyticsService(FactWorkTripDao factWorkTripDao) {
        this.factWorkTripDao = factWorkTripDao;
    }

    /**
     * Get top employees with most work trips
     * 
     * @param limit Number of employees to return
     * @return List of employee work trip statistics
     */
    public List<WorkTripStatsByEmployeeDTO> getTopEmployeesByWorkTrips(int limit) {
        return factWorkTripDao.getTopEmployeesByWorkTrips(limit);
    }

    /**
     * Get top destinations for work trips
     * 
     * @param limit Number of destinations to return
     * @return List of destination statistics
     */
    public List<WorkTripStatsByDestinationDTO> getTopDestinations(int limit) {
        return factWorkTripDao.getTopDestinations(limit);
    }

    /**
     * Get work trip costs by department
     * 
     * @return List of department work trip cost statistics
     */
    public List<WorkTripCostByDepartmentDTO> getWorkTripCostByDepartment() {
        return factWorkTripDao.getWorkTripCostByDepartment();
    }

    /**
     * Get work trip statistics by purpose
     * 
     * @return List of work trip purpose statistics
     */
    public List<WorkTripPurposeStatsDTO> getWorkTripPurposeStats() {
        return factWorkTripDao.getWorkTripPurposeStats();
    }

    /**
     * Get work trip trends by month
     * 
     * @return List of work trip monthly trends
     */
    public List<WorkTripTrendByMonthDTO> getWorkTripTrendByMonth() {
        return factWorkTripDao.getWorkTripTrendByMonth();
    }

    /**
     * Get work trip costs by position
     * 
     * @return List of position work trip cost statistics
     */
    public List<WorkTripCostByPositionDTO> getWorkTripCostByPosition() {
        return factWorkTripDao.getWorkTripCostByPosition();
    }
}