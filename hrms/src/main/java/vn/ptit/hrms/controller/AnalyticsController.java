package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ptit.hrms.service.AnalyticsService;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/pages/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get current year and month
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue() - 1;

        // Get analytics data from data warehouse
        Map<String, Double> attendanceByDept = analyticsService.getAverageAttendanceByDepartment(currentYear, currentMonth);
        Map<String, Integer> attendanceStatus = analyticsService.getAttendanceStatusDistribution(currentYear, currentMonth);

        // Add data to model
        model.addAttribute("attendanceByDept", attendanceByDept);
        model.addAttribute("attendanceStatus", attendanceStatus);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);

        return "pages/analytics/dashboard";
    }

    @GetMapping("/attendance")
    public String attendanceAnalytics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        // Default to current year/month if not specified
        year = Optional.ofNullable(year).orElse(LocalDate.now().getYear());
        month = Optional.ofNullable(month).orElse(LocalDate.now().getMonthValue());

        // Get attendance analytics
        Map<String, Double> attendanceByDept = analyticsService.getAverageAttendanceByDepartment(year, month);
        Map<String, Integer> attendanceStatus = analyticsService.getAttendanceStatusDistribution(year, month);

        // Add data to model
        model.addAttribute("attendanceByDept", attendanceByDept);
        model.addAttribute("attendanceStatus", attendanceStatus);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);

        return "pages/analytics/attendance";
    }

    // Add more analytics endpoints as needed
}