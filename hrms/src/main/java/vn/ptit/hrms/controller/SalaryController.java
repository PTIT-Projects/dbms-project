package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.primary.Salary;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.SalaryService;

@Controller
@RequestMapping("/admin/pages/salaries")
public class SalaryController {

    private final SalaryService salaryService;
    private final EmployeeService employeeService;

    public SalaryController(SalaryService salaryService, EmployeeService employeeService) {
        this.salaryService = salaryService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllSalaries(Model model) {
        model.addAttribute("salaries", salaryService.getAllSalaries());
        return "pages/salary/list";
    }

    @GetMapping("/{id}")
    public String getSalaryById(@PathVariable Integer id, Model model) {
        model.addAttribute("salary", salaryService.getSalaryById(id));
        return "pages/salary/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("salary", new Salary());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/salary/create";
    }

    @PostMapping
    public String createSalary(@ModelAttribute Salary salary) {
        salaryService.createSalary(salary);
        return "redirect:/admin/pages/salaries";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("salary", salaryService.getSalaryById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/salary/edit";
    }

    @PostMapping("/{id}")
    public String updateSalary(@PathVariable Integer id, @ModelAttribute Salary salary) {
        // Ensure the ID is set correctly
        salary.setId(id);
        
        // Calculate net salary based on inputs before saving
        if (salary.getBasicSalary() == null) salary.setBasicSalary(0.0);
        if (salary.getAllowance() == null) salary.setAllowance(0.0);
        if (salary.getDeductions() == null) salary.setDeductions(0.0);
        
        Double netSalary = salary.getBasicSalary() + salary.getAllowance() - salary.getDeductions();
        salary.setNetSalary(netSalary);
        
        // Update the salary record
        salaryService.updateSalary(salary);
        return "redirect:/admin/pages/salaries";
    }

    @GetMapping("/{id}/delete")
    public String deleteSalary(@PathVariable Integer id) {
        salaryService.deleteSalary(id);
        return "redirect:/admin/pages/salaries";
    }
}