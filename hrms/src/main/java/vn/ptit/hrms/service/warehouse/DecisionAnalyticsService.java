package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactDecisionDao;
import vn.ptit.hrms.dto.warehouse.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DecisionAnalyticsService {
    private final FactDecisionDao factDecisionDao;

    public DecisionAnalyticsService(FactDecisionDao factDecisionDao) {
        this.factDecisionDao = factDecisionDao;
    }

    // Số lượng quyết định theo loại và phòng ban
    public List<DecisionByDepartmentTypeDTO> getDecisionsByDepartmentAndType() {
        return factDecisionDao.getDecisionsByDepartmentAndType();
    }

    // Phân tích số lượng quyết định theo tháng/năm
    public List<DecisionTrendDTO> getDecisionTrendsByTime() {
        return factDecisionDao.getDecisionTrendsByTime();
    }

    // Tỷ lệ khen thưởng vs kỷ luật
    public List<DecisionTypeRatioDTO> getDecisionTypeRatios() {
        return factDecisionDao.getDecisionTypeRatios();
    }

    // Ảnh hưởng đến hành vi làm việc
    public List<EmployeeBehaviorAfterDecisionDTO> getEmployeeBehaviorAfterDisciplinary() {
        return factDecisionDao.getEmployeeBehaviorAfterDisciplinary();
    }

    // Thời gian hiệu lực trung bình của quyết định
    public List<DecisionDurationDTO> getAverageDecisionDurations() {
        return factDecisionDao.getAverageDecisionDurations();
    }

    // Top nhân viên được khen thưởng nhiều nhất
    public List<TopRewardedEmployeeDTO> getTopRewardedEmployees(int limit) {
        return factDecisionDao.getTopRewardedEmployees(limit);
    }

    // Tương quan giữa khen thưởng và giờ làm thêm
    public List<RewardOvertimeCorrelationDTO> getRewardOvertimeCorrelation() {
        return factDecisionDao.getRewardOvertimeCorrelation();
    }

    // Phân tích theo chức vụ
    public List<DecisionByPositionDTO> getDecisionsByPosition() {
        return factDecisionDao.getDecisionsByPosition();
    }
    
    // Group decision trends by decision type for easier charting
    public Map<String, List<DecisionTrendDTO>> getDecisionTrendsByTimeGrouped() {
        List<DecisionTrendDTO> allTrends = getDecisionTrendsByTime();
        return allTrends.stream()
            .collect(Collectors.groupingBy(DecisionTrendDTO::getDecisionType));
    }
    
    // Group department decisions by decision type
    public Map<String, List<DecisionByDepartmentTypeDTO>> getDecisionsByDepartmentGrouped() {
        List<DecisionByDepartmentTypeDTO> allDecisions = getDecisionsByDepartmentAndType();
        return allDecisions.stream()
            .collect(Collectors.groupingBy(DecisionByDepartmentTypeDTO::getDecisionType));
    }
    
    // Get correlation coefficient between rewards and overtime hours
    public double calculateRewardOvertimeCorrelation() {
        List<RewardOvertimeCorrelationDTO> data = getRewardOvertimeCorrelation();
        
        // Filter out entries with zero overtime (may skew results)
        data = data.stream()
            .filter(d -> d.getTotalOvertimeHours() > 0)
            .collect(Collectors.toList());
        
        if (data.size() < 2) {
            return 0.0; // Not enough data for correlation
        }
        
        double sumX = data.stream().mapToDouble(RewardOvertimeCorrelationDTO::getRewardCount).sum();
        double sumY = data.stream().mapToDouble(RewardOvertimeCorrelationDTO::getTotalOvertimeHours).sum();
        double sumXY = data.stream().mapToDouble(d -> d.getRewardCount() * d.getTotalOvertimeHours()).sum();
        double sumXSquare = data.stream().mapToDouble(d -> d.getRewardCount() * d.getRewardCount()).sum();
        double sumYSquare = data.stream().mapToDouble(d -> d.getTotalOvertimeHours() * d.getTotalOvertimeHours()).sum();
        int n = data.size();
        
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumXSquare - sumX * sumX) * (n * sumYSquare - sumY * sumY));
        
        return denominator == 0 ? 0 : numerator / denominator;
    }
    
    // Get overall decision counts
    public Map<String, Long> getTotalDecisionsByType() {
        List<DecisionByDepartmentTypeDTO> allDecisions = getDecisionsByDepartmentAndType();
        return allDecisions.stream()
            .collect(Collectors.groupingBy(
                DecisionByDepartmentTypeDTO::getDecisionType,
                Collectors.summingLong(DecisionByDepartmentTypeDTO::getTotalDecisions)));
    }
}