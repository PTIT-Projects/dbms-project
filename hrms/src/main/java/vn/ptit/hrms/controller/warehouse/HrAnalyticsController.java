package vn.ptit.hrms.controller.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ptit.hrms.dto.warehouse.*;
import vn.ptit.hrms.service.warehouse.*;

import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("admin/analytics")
public class HrAnalyticsController {
    private final AttendanceAnalyticsService attendanceAnalyticsService;
    private final SalaryAnalyticsService salaryAnalyticsService;
    private final LeaveAnalyticsService leaveAnalyticsService;
    private final WorkTripAnalyticsService workTripAnalyticsService;
    private final FactRecruitmentPlanService factRecruitmentPlanService;
    private final RegistrationAnalyticsService registrationAnalyticsService;

    public HrAnalyticsController(
            AttendanceAnalyticsService attendanceAnalyticsService,
            SalaryAnalyticsService salaryAnalyticsService,
            LeaveAnalyticsService leaveAnalyticsService,
            WorkTripAnalyticsService workTripAnalyticsService,
            FactRecruitmentPlanService factRecruitmentPlanService,
            RegistrationAnalyticsService registrationAnalyticsService) {
        this.attendanceAnalyticsService = attendanceAnalyticsService;
        this.salaryAnalyticsService = salaryAnalyticsService;
        this.leaveAnalyticsService = leaveAnalyticsService;
        this.workTripAnalyticsService = workTripAnalyticsService;
        this.factRecruitmentPlanService = factRecruitmentPlanService;
        this.registrationAnalyticsService = registrationAnalyticsService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Add some high-level summary stats
        int currentYear = Year.now().getValue();
        model.addAttribute("currentYear", currentYear);
        return "pages/analytics/dashboard";
    }

    @GetMapping("/attendance")
    public String showAttendanceAnalytics(
            Model model,
            @RequestParam(required = false, defaultValue = "5") int limit,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        if (year == null) {
            year = Year.now().getValue();
        }

        // Load attendance analytics data
        model.addAttribute("employeeWorkSummary", attendanceAnalyticsService.getEmployeeWorkSummaryByYear(year));
        model.addAttribute("topLateDepartments", attendanceAnalyticsService.getTopLateDepartments(limit));
        model.addAttribute("topOvertimeEmployees", attendanceAnalyticsService.getTopEmployeesByOvertime(limit));
        model.addAttribute("departmentPerformance", attendanceAnalyticsService.getDepartmentPerformanceStats());

        // ADD THIS CODE: Create and set the attendanceSummary attribute
        Map<String, Object> attendanceSummary = new HashMap<>();
        attendanceSummary.put("avgAttendanceRate", 95.2); // Default or calculated value
        attendanceSummary.put("avgWorkHours", 7.8);       // Default or calculated value
        attendanceSummary.put("lateCount", 52);           // Default or calculated value
        attendanceSummary.put("latePercentage", 5.2);     // Default or calculated value
        attendanceSummary.put("absentRate", 3.1);         // Default or calculated value
        model.addAttribute("attendanceSummary", attendanceSummary);

        // You also need to add the following attributes used in the template:
        model.addAttribute("attendanceTrends", Collections.emptyList());      // Add your real data here
        model.addAttribute("attendanceStatus", Collections.emptyMap());       // Add your real data here
        model.addAttribute("departmentAttendance", Collections.emptyList());  // Add your real data here
        model.addAttribute("overtimeData", Collections.emptyList());          // Add your real data here
        model.addAttribute("workHoursByDay", Collections.emptyList());        // Add your real data here
        model.addAttribute("attendanceIssues", Collections.emptyList());      // Add your real data here

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("limit", limit);

        return "pages/analytics/attendance";
    }

    @GetMapping("/salary")
    public String showSalaryAnalytics(
            Model model,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) Integer year) {
        
        if (year == null) {
            year = Year.now().getValue();
        }
        
        // Load salary analytics data
        model.addAttribute("salaryByDeptPosition", salaryAnalyticsService.getSalaryStatsByDeptPosition());
        model.addAttribute("highestPaidEmployees", salaryAnalyticsService.getTopEmployeesByHighestSalary(limit));
        model.addAttribute("lowestPaidEmployees", salaryAnalyticsService.getTopEmployeesByLowestSalary(limit));
        model.addAttribute("salaryRatioByDepartment", salaryAnalyticsService.getSalaryRatioByDepartment());
        model.addAttribute("salaryRatioByEmployee", salaryAnalyticsService.getSalaryRatioByEmployee());
        model.addAttribute("salaryTrends", salaryAnalyticsService.getSalaryTrendByMonth(year));
        model.addAttribute("salaryByAgeGender", salaryAnalyticsService.getSalaryByAgeGender());
        
        model.addAttribute("year", year);
        model.addAttribute("limit", limit);
        
        return "pages/analytics/salary";
    }

    @GetMapping("/leave")
    public String showLeaveAnalytics(
            Model model,
            @RequestParam(required = false, defaultValue = "3") int maxRemainingDays) {
        
        // Load leave analytics data
        model.addAttribute("leaveByDeptPosition", leaveAnalyticsService.getLeaveStatsByDeptPosition());
        model.addAttribute("lowRemainingLeave", leaveAnalyticsService.getEmployeesWithLowRemainingLeave(maxRemainingDays));
        model.addAttribute("leaveTrendByMonth", leaveAnalyticsService.getLeaveTrendByMonth());
        model.addAttribute("leaveTrendByQuarter", leaveAnalyticsService.getLeaveTrendByQuarter());
        model.addAttribute("leaveTypeStats", leaveAnalyticsService.getLeaveTypeStats());
        model.addAttribute("leaveByExperience", leaveAnalyticsService.getLeaveByExperience());
        
        model.addAttribute("maxRemainingDays", maxRemainingDays);
        
        return "pages/analytics/leave";
    }

    @GetMapping("/worktrip")
    public String showWorkTripAnalytics(
            Model model,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        // Load work trip analytics data
        model.addAttribute("topEmployees", workTripAnalyticsService.getTopEmployeesByWorkTrips(limit));
        model.addAttribute("topDestinations", workTripAnalyticsService.getTopDestinations(limit));
        model.addAttribute("costByDepartment", workTripAnalyticsService.getWorkTripCostByDepartment());
        model.addAttribute("purposeStats", workTripAnalyticsService.getWorkTripPurposeStats());
        model.addAttribute("trendByMonth", workTripAnalyticsService.getWorkTripTrendByMonth());
        model.addAttribute("costByPosition", workTripAnalyticsService.getWorkTripCostByPosition());
        
        model.addAttribute("limit", limit);
        
        return "pages/analytics/worktrip";
    }

    @GetMapping("/recruitment")
    public String showRecruitmentAnalytics(
            Model model,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        
        // Get overall recruitment stats
        RecruitmentPlanByDepartmentPositionDTO overallStats = factRecruitmentPlanService.getOverallRecruitmentStats();
        model.addAttribute("overallStats", overallStats);
        
        // Get top departments with open positions
        List<RecruitmentPlanByDepartmentPositionDTO> topDepartments = 
            factRecruitmentPlanService.getTopDepartmentsWithOpenPositions(limit);
        model.addAttribute("topDepartments", topDepartments);
        
        // Get top positions with open positions
        List<RecruitmentPlanByDepartmentPositionDTO> topPositions = 
            factRecruitmentPlanService.getTopPositionsWithOpenPositions(limit);
        model.addAttribute("topPositions", topPositions);
        
        // Get all recruitment plans by department and position
        List<RecruitmentPlanByDepartmentPositionDTO> allPlans = 
            factRecruitmentPlanService.getAllRecruitmentPlansByDepartmentPosition();
        model.addAttribute("allPlans", allPlans);
        
        model.addAttribute("limit", limit);
        
        return "pages/analytics/recruitment";
    }

    @GetMapping("/registration")
    public String showRegistrationAnalytics(
            Model model,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        
        // Get registration data for analysis
        model.addAttribute("departmentTypeCounts", registrationAnalyticsService.getRegistrationsByDepartmentAndType());
        model.addAttribute("employeeTypeCounts", registrationAnalyticsService.getRegistrationsByEmployeeAndType());
        model.addAttribute("approvalRates", registrationAnalyticsService.getRegistrationApprovalRatesByType());
        model.addAttribute("registrationTrends", registrationAnalyticsService.getRegistrationTrendsByTime());
        model.addAttribute("topDepartments", registrationAnalyticsService.getTopDepartmentsByRegistrationCount(limit));
        model.addAttribute("statusCounts", registrationAnalyticsService.getRegistrationsByStatusAndType());
        model.addAttribute("statusCountsGrouped", registrationAnalyticsService.getRegistrationsByStatusAndTypeGrouped());
        model.addAttribute("averageOvertimeHours", registrationAnalyticsService.getAverageApprovedOvertimeHours());
        model.addAttribute("trendsByType", registrationAnalyticsService.getRegistrationTrendsByTimeGrouped());
        model.addAttribute("totalsByType", registrationAnalyticsService.getTotalRegistrationsByType());
        
        model.addAttribute("limit", limit);
        
        return "pages/analytics/registration";
    }
}