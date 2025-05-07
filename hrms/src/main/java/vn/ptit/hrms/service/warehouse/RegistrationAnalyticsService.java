package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactRegistrationDao;
import vn.ptit.hrms.dto.warehouse.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegistrationAnalyticsService {
    private final FactRegistrationDao factRegistrationDao;

    public RegistrationAnalyticsService(FactRegistrationDao factRegistrationDao) {
        this.factRegistrationDao = factRegistrationDao;
    }

    // Get registrations by department and type
    public List<RegistrationByDepartmentTypeDTO> getRegistrationsByDepartmentAndType() {
        return factRegistrationDao.getRegistrationsByDepartmentAndType();
    }

    // Get registrations by employee and type
    public List<RegistrationByEmployeeTypeDTO> getRegistrationsByEmployeeAndType() {
        return factRegistrationDao.getRegistrationsByEmployeeAndType();
    }

    // Get approval rates by registration type
    public List<RegistrationApprovalRateDTO> getRegistrationApprovalRatesByType() {
        return factRegistrationDao.getRegistrationApprovalRatesByType();
    }

    // Get registration trends over time
    public List<RegistrationTrendDTO> getRegistrationTrendsByTime() {
        return factRegistrationDao.getRegistrationTrendsByTime();
    }

    // Get top departments with most registrations
    public List<RegistrationByDepartmentTypeDTO> getTopDepartmentsByRegistrationCount(int limit) {
        return factRegistrationDao.getTopDepartmentsByRegistrationCount(limit);
    }

    // Get registrations by status and type
    public List<RegistrationByStatusDTO> getRegistrationsByStatusAndType() {
        return factRegistrationDao.getRegistrationsByStatusAndType();
    }
    
    // Get registrations by status and type as a map
    public Map<String, List<RegistrationByStatusDTO>> getRegistrationsByStatusAndTypeGrouped() {
        List<RegistrationByStatusDTO> allStats = factRegistrationDao.getRegistrationsByStatusAndType();
        return allStats.stream()
            .collect(Collectors.groupingBy(RegistrationByStatusDTO::getRegistrationType));
    }

    // Get average approved overtime hours
    public double getAverageApprovedOvertimeHours() {
        return factRegistrationDao.getAverageApprovedOvertimeHours();
    }
    
    // Get registration trends by type as a map for charting
    public Map<String, List<RegistrationTrendDTO>> getRegistrationTrendsByTimeGrouped() {
        List<RegistrationTrendDTO> allTrends = factRegistrationDao.getRegistrationTrendsByTime();
        return allTrends.stream()
            .collect(Collectors.groupingBy(RegistrationTrendDTO::getRegistrationType));
    }
    
    // Get total count of registrations by type
    public Map<String, Long> getTotalRegistrationsByType() {
        List<RegistrationByDepartmentTypeDTO> allRegistrations = factRegistrationDao.getRegistrationsByDepartmentAndType();
        return allRegistrations.stream()
            .collect(Collectors.groupingBy(
                RegistrationByDepartmentTypeDTO::getRegistrationType,
                Collectors.summingLong(RegistrationByDepartmentTypeDTO::getTotalRegistrations)));
    }
}