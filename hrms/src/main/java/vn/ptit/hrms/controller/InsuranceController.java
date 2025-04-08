package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Insurance;
import vn.ptit.hrms.service.InsuranceService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/admin/pages/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final EmployeeService employeeService;

    public InsuranceController(InsuranceService insuranceService, EmployeeService employeeService) {
        this.insuranceService = insuranceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllInsurance(Model model) {
        model.addAttribute("insuranceRecords", insuranceService.getAllInsurances());
        return "pages/insurance/list";
    }

    @GetMapping("/{id}")
    public String getInsuranceById(@PathVariable Integer id, Model model) {
        model.addAttribute("insurance", insuranceService.getInsuranceById(id));
        return "pages/insurance/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("insurance", new Insurance());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/insurance/create";
    }

    @PostMapping
    public String createInsurance(@ModelAttribute Insurance insurance) {
        insuranceService.createInsurance(insurance);
        return "redirect:/admin/pages/insurances";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("insurance", insuranceService.getInsuranceById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/insurance/edit";
    }

    @PostMapping("/{id}")
    public String updateInsurance(@PathVariable Integer id, @ModelAttribute Insurance insurance) {
        insurance.setId(id);
        insuranceService.updateInsurance(insurance);
        return "redirect:/admin/pages/insurances";
    }

    @GetMapping("/{id}/delete")
    public String deleteInsurance(@PathVariable Integer id) {
        insuranceService.deleteInsurance(id);
        return "redirect:/admin/pages/insurances";
    }
}