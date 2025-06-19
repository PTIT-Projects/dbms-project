package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ptit.hrms.dao.primary.ApplicantDao;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.RegistrationService;

@Controller
public class AdminController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final RegistrationService registrationService;

    public AdminController(
                           EmployeeService employeeService,
                           DepartmentService departmentService,
                           RegistrationService registrationService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.registrationService = registrationService;
    }

    @GetMapping({"/", "/admin", "/admin/index", "index.html", "/dashboard"})
    public String dashboard(Model model) {
        // Add dashboard data
        model.addAttribute("employeeCount", employeeService.getEmployeeCount());
        model.addAttribute("departmentCount", departmentService.getDepartmentCount());
        model.addAttribute("pendingRequestCount", registrationService.getPendingRegistrationCount());
        return "index";
    }


    @GetMapping("/analytics")
    public String redirectToAnalytics() {
        return "redirect:http://localhost:3000";
    }
}