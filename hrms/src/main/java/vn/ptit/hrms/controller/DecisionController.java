package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Decision;
import vn.ptit.hrms.service.DecisionService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/admin/pages/decisions")
public class DecisionController {

    private final DecisionService decisionService;
    private final EmployeeService employeeService;

    public DecisionController(DecisionService decisionService, EmployeeService employeeService) {
        this.decisionService = decisionService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllDecisions(Model model) {
        model.addAttribute("decisions", decisionService.getAllDecisions());
        return "pages/decision/list";
    }

    @GetMapping("/{id}")
    public String getDecisionById(@PathVariable Integer id, Model model) {
        model.addAttribute("decision", decisionService.getDecisionById(id));
        return "pages/decision/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("decision", new Decision());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/decision/create";
    }

    @PostMapping
    public String createDecision(@ModelAttribute Decision decision) {
        decisionService.createDecision(decision);
        return "redirect:/admin/pages/decisions";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("decision", decisionService.getDecisionById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/decision/edit";
    }

    @PostMapping("/{id}")
    public String updateDecision(@PathVariable Integer id, @ModelAttribute Decision decision) {
        decision.setId(id);
        decisionService.updateDecision(decision);
        return "redirect:/admin/pages/decisions";
    }

    @GetMapping("/{id}/delete")
    public String deleteDecision(@PathVariable Integer id) {
        decisionService.deleteDecision(id);
        return "redirect:/admin/pages/decisions";
    }
}