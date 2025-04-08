package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Registration;
import vn.ptit.hrms.service.RegistrationService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/admin/pages/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final EmployeeService employeeService;

    public RegistrationController(RegistrationService registrationService, EmployeeService employeeService) {
        this.registrationService = registrationService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllRegistrations(Model model) {
        model.addAttribute("registrations", registrationService.getAllRegistrations());
        return "pages/registration/list";
    }

    @GetMapping("/{id}")
    public String getRegistrationById(@PathVariable Integer id, Model model) {
        model.addAttribute("registration", registrationService.getRegistrationById(id));
        return "pages/registration/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("registration", new Registration());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("approvers", employeeService.getAllEmployees());
        return "pages/registration/create";
    }

    @PostMapping
    public String createRegistration(@ModelAttribute Registration registration) {
        registrationService.createRegistration(registration);
        return "redirect:/admin/pages/registrations";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("registration", registrationService.getRegistrationById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("approvers", employeeService.getAllEmployees());
        return "pages/registration/edit";
    }

    @PostMapping("/{id}")
    public String updateRegistration(@PathVariable Integer id, @ModelAttribute Registration registration) {
        registration.setId(id);
        registrationService.updateRegistration(registration);
        return "redirect:/admin/pages/registrations";
    }

    @GetMapping("/{id}/delete")
    public String deleteRegistration(@PathVariable Integer id) {
        registrationService.deleteRegistration(id);
        return "redirect:/admin/pages/registrations";
    }

    @GetMapping("/{id}/approve")
    public String approveRegistration(@PathVariable Integer id, @RequestParam Integer approverId) {
        registrationService.approveRegistration(id, approverId);
        return "redirect:/admin/pages/registrations";
    }

    @GetMapping("/{id}/reject")
    public String rejectRegistration(@PathVariable Integer id, @RequestParam Integer approverId) {
        registrationService.rejectRegistration(id, approverId);
        return "redirect:/registrations";
    }
}