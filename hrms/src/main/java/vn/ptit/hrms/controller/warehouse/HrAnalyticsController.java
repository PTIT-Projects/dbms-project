package vn.ptit.hrms.controller.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ptit.hrms.service.warehouse.*;

import java.time.Year;

@Controller
@RequestMapping("admin/analytics")
public class HrAnalyticsController {
    private final AttendanceAnalyticsService attendanceAnalyticsService;
    private final SalaryAnalyticsService salaryAnalyticsService;
    private final LeaveAnalyticsService leaveAnalyticsService;
    private final WorkTripAnalyticsService workTripAnalyticsService;

    public HrAnalyticsController(
            AttendanceAnalyticsService attendanceAnalyticsService,
            SalaryAnalyticsService salaryAnalyticsService,
            LeaveAnalyticsService leaveAnalyticsService,
            WorkTripAnalyticsService workTripAnalyticsService) {
        this.attendanceAnalyticsService = attendanceAnalyticsService;
        this.salaryAnalyticsService = salaryAnalyticsService;
        this.leaveAnalyticsService = leaveAnalyticsService;
        this.workTripAnalyticsService = workTripAnalyticsService;
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
            @RequestParam(required = false) Integer year) {
        
        if (year == null) {
            year = Year.now().getValue();
        }
        
        // Load attendance analytics data
        model.addAttribute("employeeWorkSummary", attendanceAnalyticsService.getEmployeeWorkSummaryByYear(year));
        model.addAttribute("topLateDepartments", attendanceAnalyticsService.getTopLateDepartments(limit));
        model.addAttribute("topOvertimeEmployees", attendanceAnalyticsService.getTopEmployeesByOvertime(limit));
        model.addAttribute("departmentPerformance", attendanceAnalyticsService.getDepartmentPerformanceStats());
        
        model.addAttribute("year", year);
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
}