package vn.ptit.hrms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.PositionService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/pages/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;

    public EmployeeController(EmployeeService employeeService,
                              DepartmentService departmentService,
                              PositionService positionService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.positionService = positionService;
    }

    @GetMapping("/list")
    public String getAllEmployees(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(required = false) String search,
                                  @RequestParam(required = false) Integer departmentId,
                                  @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.getEmployeesPage(pageable, search, departmentId, status);

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("page", employeePage);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("search", search);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("status", status);

        return "pages/employee/list";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "pages/employee/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", positionService.getAllPositions());
        return "pages/employee/create";
    }

    @PostMapping
    public String createEmployee(@ModelAttribute Employee employee) {
        employeeService.createEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", positionService.getAllPositions());
        return "pages/employee/edit";
    }

    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Integer id, @ModelAttribute Employee employee) {
        employee.setId(id);
        employeeService.updateEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}