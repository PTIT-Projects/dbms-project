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
import vn.ptit.hrms.constant.EmployeeStatusEnum;
import vn.ptit.hrms.constant.GenderEnum;
import vn.ptit.hrms.domain.Employee;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.EmployeeService;
import vn.ptit.hrms.service.PositionService;

import java.time.LocalDate;
import java.util.ArrayList;

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
//        if (!employeeService.isExistsEmployeeWithEmail("admin@gmail.com")) {
//            Employee employee = new Employee();
//            employee.setFullName("Admin");
//            employee.setDateOfBirth(LocalDate.of(1995, 9, 24));
//            employee.setGender(GenderEnum.MALE);
//            employee.setAddress("Hà Nội");
//            employee.setPhone("09182317241");
//            employee.setDepartment(this.departmentService.getAllDepartments().get(0));
//            employee.setPosition(this.positionService.getAllPositions().get(0));
//            employee.setHireDate(LocalDate.now());
//            employee.setStatus(EmployeeStatusEnum.ACTIVE);
//            employee.setRoleName("ROLE_ADMIN");
//            employee.setEmail("admin@gmail.com");
//            employee.setPassword(this.passwordEncoder.encode("123456"));
//            this.employeeService.createEmployee(employee);
//
//
//        }
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        return "auth/login";
    }



}