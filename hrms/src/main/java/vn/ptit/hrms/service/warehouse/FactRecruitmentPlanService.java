package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactRecruitmentPlanDao;
import vn.ptit.hrms.dto.warehouse.RecruitmentPlanByDepartmentPositionDTO;

import java.util.List;

@Service
public class FactRecruitmentPlanService {
    private final FactRecruitmentPlanDao factRecruitmentPlanDao;
    
    public FactRecruitmentPlanService(FactRecruitmentPlanDao factRecruitmentPlanDao) {
        this.factRecruitmentPlanDao = factRecruitmentPlanDao;
    }
    
    /**
     * Get all recruitment plans grouped by department and position
     */
    public List<RecruitmentPlanByDepartmentPositionDTO> getAllRecruitmentPlansByDepartmentPosition() {
        return factRecruitmentPlanDao.getRecruitmentPlansByDepartmentPosition();
    }
    
    /**
     * Get top departments with the most open positions
     * @param limit Maximum number of departments to return
     */
    public List<RecruitmentPlanByDepartmentPositionDTO> getTopDepartmentsWithOpenPositions(int limit) {
        return factRecruitmentPlanDao.getTopDepartmentsWithOpenPositions(limit);
    }
    
    /**
     * Get top positions with the most open positions
     * @param limit Maximum number of positions to return
     */
    public List<RecruitmentPlanByDepartmentPositionDTO> getTopPositionsWithOpenPositions(int limit) {
        return factRecruitmentPlanDao.getTopPositionsWithOpenPositions(limit);
    }
    
    /**
     * Calculate overall recruitment completion statistics
     * @return Object with total positions, filled positions, and remaining positions
     */
    public RecruitmentPlanByDepartmentPositionDTO getOverallRecruitmentStats() {
        List<RecruitmentPlanByDepartmentPositionDTO> allPlans = getAllRecruitmentPlansByDepartmentPosition();
        
        RecruitmentPlanByDepartmentPositionDTO overall = new RecruitmentPlanByDepartmentPositionDTO();
        overall.setDepartmentName("All Departments");
        overall.setPositionName("All Positions");
        
        int totalPositions = 0;
        int remainingPositions = 0;
        int filledPositions = 0;
        
        for (RecruitmentPlanByDepartmentPositionDTO plan : allPlans) {
            totalPositions += plan.getQuantity();
            remainingPositions += plan.getRemainingPositions();
            filledPositions += plan.getFilledPositions();
        }
        
        overall.setQuantity(totalPositions);
        overall.setRemainingPositions(remainingPositions);
        overall.setFilledPositions(filledPositions);
        
        return overall;
    }
}
