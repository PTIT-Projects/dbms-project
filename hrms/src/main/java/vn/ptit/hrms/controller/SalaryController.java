package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Salary;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.SalaryService;

@Controller
@RequestMapping("/salaries")
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
        return "redirect:/salaries";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("salary", salaryService.getSalaryById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/salary/edit";
    }

    @PostMapping("/{id}")
    public String updateSalary(@PathVariable Integer id, @ModelAttribute Salary salary) {
        salary.setId(id);
        salaryService.updateSalary(salary);
        return "redirect:/salaries";
    }

    @GetMapping("/{id}/delete")
    public String deleteSalary(@PathVariable Integer id) {
        salaryService.deleteSalary(id);
        return "redirect:/salaries";
    }
}