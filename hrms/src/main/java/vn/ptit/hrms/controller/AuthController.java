package vn.ptit.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.PositionService;

@Controller
public class AuthController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(EmployeeService employeeService,
                          DepartmentService departmentService,
                          PositionService positionService,
                          PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", positionService.getAllPositions());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        // Check if email exists
        try {
            Employee existingEmployee = employeeService.getEmployeeById(employee.getId());
            if (existingEmployee != null) {
                redirectAttributes.addFlashAttribute("error", "Email already exists");
                return "redirect:/register";
            }
        } catch (Exception e) {
            // Employee does not exist, continue with registration
        }

        // Encode password
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        // Set default role
        employee.setRoleName("ROLE_USER");

        // Save employee
        employeeService.createEmployee(employee);

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/index";
    }
}