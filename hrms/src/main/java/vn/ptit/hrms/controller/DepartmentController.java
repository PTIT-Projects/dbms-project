package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.primary.Department;
import vn.ptit.hrms.domain.primary.DepartmentManager;
import vn.ptit.hrms.domain.primary.Employee;
import vn.ptit.hrms.service.DepartmentManagerService;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/pages/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentManagerService departmentManagerService;
    private final EmployeeService employeeService;

    public DepartmentController(DepartmentService departmentService,
                               DepartmentManagerService departmentManagerService,
                               EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.departmentManagerService = departmentManagerService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentManager> departmentManagers = departmentManagerService.getAllDepartmentManagers();
        
        // Create a map for easy access of department managers by department ID
        Map<Integer, DepartmentManager> managerMap = new HashMap<>();
        for (DepartmentManager manager : departmentManagers) {
            if (manager.getDepartment() != null) {
                managerMap.put(manager.getDepartment().getId(), manager);
            }
        }
        
        model.addAttribute("departments", departments);
        model.addAttribute("departmentManagers", managerMap);
        return "pages/department/list";
    }

    @GetMapping("/{id}")
    public String getDepartmentById(@PathVariable Integer id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        
        // Get department manager
        try {
            DepartmentManager departmentManager = departmentManagerService.getDepartmentManagerByDepartmentId(id);
            model.addAttribute("departmentManager", departmentManager);
        } catch (Exception e) {
            // Manager not found for this department
            model.addAttribute("departmentManager", null);
        }
        
        // Get employees in this department
        List<Employee> employees = employeeService.getEmployeesByDepartmentId(id);
        model.addAttribute("employees", employees);
        model.addAttribute("employeeCount", employees.size());
        
        return "pages/department/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "pages/department/create";
    }

    @PostMapping
    public String createDepartment(@ModelAttribute Department department) {
        departmentService.createDepartment(department);
        return "redirect:/admin/pages/departments";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("department", departmentService.getDepartmentById(id));
        return "pages/department/edit";
    }

    @PostMapping("/{id}")
    public String updateDepartment(@PathVariable Integer id, @ModelAttribute Department department) {
        department.setId(id);
        departmentService.updateDepartment(department);
        return "redirect:/admin/pages/departments";
    }

    @GetMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return "redirect:/admin/pages/departments";
    }
}