package vn.ptit.hrms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.constant.AttendanceStatusEnum;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.domain.Attendance;
import vn.ptit.hrms.service.AttendanceService;
import vn.ptit.hrms.service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/pages/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllAttendance(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String employeeSearch,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Attendance> attendancePage = attendanceService.findAttendancePage(
                pageable, employeeSearch, startDate, endDate, status);

        model.addAttribute("attendanceRecords", attendancePage.getContent());
        model.addAttribute("currentPage", attendancePage.getNumber());
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalItems", attendancePage.getTotalElements());
        model.addAttribute("employeeSearch", employeeSearch);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("status", status);

        return "pages/attendance/list";
    }

    @GetMapping("/{id}")
    public String getAttendanceById(@PathVariable Integer id, Model model) {
        model.addAttribute("attendance", attendanceService.getAttendanceById(id));
        return "pages/attendance/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/attendance/create";
    }

    @PostMapping
    public String createAttendance(@ModelAttribute Attendance attendance) {
        // Set default status if not provided
        if (attendance.getStatus() == null) {
            attendance.setStatus(AttendanceStatusEnum.PRESENT);
        }

        attendanceService.createAttendance(attendance);
        return "redirect:/admin/pages/attendances";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("attendance", attendanceService.getAttendanceById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/attendance/edit";
    }

    @PostMapping("/{id}")
    public String updateAttendance(@PathVariable Integer id, @ModelAttribute Attendance attendance) {
        attendance.setId(id);
        attendanceService.updateAttendance(attendance);
        return "redirect:/admin/pages/attendances";
    }

    @GetMapping("/{id}/delete")
    public String deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/admin/pages/attendances";
    }
}