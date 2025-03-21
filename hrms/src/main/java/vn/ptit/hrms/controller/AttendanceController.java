package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Attendance;
import vn.ptit.hrms.service.AttendanceService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllAttendance(Model model) {
        model.addAttribute("attendanceRecords", attendanceService.getAllAttendance());
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
        attendanceService.createAttendance(attendance);
        return "redirect:/attendance";
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
        return "redirect:/attendance";
    }

    @GetMapping("/{id}/delete")
    public String deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return "redirect:/attendance";
    }
}