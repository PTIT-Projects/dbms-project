package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.ptit.hrms.dao.ApplicantDao;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.RegistrationService;

@Controller
public class AdminController {

    private final ApplicantDao applicantDao;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final RegistrationService registrationService;

    public AdminController(ApplicantDao applicantDao,
                           EmployeeService employeeService,
                           DepartmentService departmentService,
                           RegistrationService registrationService) {
        this.applicantDao = applicantDao;
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
        model.addAttribute("recentActivities", 0);

//        model.addAttribute("recentActivities", registrationService.getRecentActivities());

        return "index";
    }

//    @GetMapping("/pages/{category}/{page}")
//    public String viewPagesDirectly(@PathVariable String category, @PathVariable String page) {
//        return "pages/" + category + "/" + page;
//    }

//    @GetMapping("/admin/pages/{category}/{page}")
//    public String viewAdminPages(@PathVariable String category, @PathVariable String page) {
//        return "pages/" + category + "/" + page;
//    }

//    @GetMapping("/test")
//    public void test() {
//        System.out.println(applicantDao.getAllApplicants());
//    }
}