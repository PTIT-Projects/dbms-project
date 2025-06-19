package vn.ptit.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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



}