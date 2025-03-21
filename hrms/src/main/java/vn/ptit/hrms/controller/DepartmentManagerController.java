package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.DepartmentManager;
import vn.ptit.hrms.service.DepartmentManagerService;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/department-managers")
public class DepartmentManagerController {

    private final DepartmentManagerService departmentManagerService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public DepartmentManagerController(DepartmentManagerService departmentManagerService,
                                       DepartmentService departmentService,
                                       EmployeeService employeeService) {
        this.departmentManagerService = departmentManagerService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllDepartmentManagers(Model model) {
        model.addAttribute("departmentManagers", departmentManagerService.getAllDepartmentManagers());
        return "pages/department-manager/list";
    }

    @GetMapping("/{departmentId}")
    public String getDepartmentManagerByDepartmentId(@PathVariable Integer departmentId, Model model) {
        model.addAttribute("departmentManager", departmentManagerService.getDepartmentManagerByDepartmentId(departmentId));
        return "pages/department-manager/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("departmentManager", new DepartmentManager());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/department-manager/create";
    }

    @PostMapping
    public String createDepartmentManager(@ModelAttribute DepartmentManager departmentManager) {
        departmentManagerService.createDepartmentManager(departmentManager);
        return "redirect:/department-managers";
    }

    @GetMapping("/{departmentId}/edit")
    public String showEditForm(@PathVariable Integer departmentId, Model model) {
        model.addAttribute("departmentManager", departmentManagerService.getDepartmentManagerByDepartmentId(departmentId));
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/department-manager/edit";
    }

    @PostMapping("/{departmentId}")
    public String updateDepartmentManager(@PathVariable Integer departmentId, @ModelAttribute DepartmentManager departmentManager) {
        departmentManager.setDepartment(departmentService.getDepartmentById(departmentId));
        departmentManagerService.updateDepartmentManager(departmentManager);
        return "redirect:/department-managers";
    }

    @GetMapping("/{departmentId}/delete")
    public String deleteDepartmentManager(@PathVariable Integer departmentId) {
        departmentManagerService.deleteDepartmentManager(departmentId);
        return "redirect:/department-managers";
    }
}