package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.LeaveBalance;
import vn.ptit.hrms.service.LeaveBalanceService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/leave-balances")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;
    private final EmployeeService employeeService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService, EmployeeService employeeService) {
        this.leaveBalanceService = leaveBalanceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllLeaveBalances(Model model) {
        model.addAttribute("leaveBalances", leaveBalanceService.getAllLeaveBalances());
        return "pages/leave/list";
    }

    @GetMapping("/{id}")
    public String getLeaveBalanceById(@PathVariable Integer id, Model model) {
        model.addAttribute("leaveBalance", leaveBalanceService.getLeaveBalanceById(id));
        return "pages/leave/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("leaveBalance", new LeaveBalance());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/leave/create";
    }

    @PostMapping
    public String createLeaveBalance(@ModelAttribute LeaveBalance leaveBalance) {
        leaveBalanceService.createLeaveBalance(leaveBalance);
        return "redirect:/leave-balances";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("leaveBalance", leaveBalanceService.getLeaveBalanceById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/leave/edit";
    }

    @PostMapping("/{id}")
    public String updateLeaveBalance(@PathVariable Integer id, @ModelAttribute LeaveBalance leaveBalance) {
        leaveBalance.setId(id);
        leaveBalanceService.updateLeaveBalance(leaveBalance);
        return "redirect:/leave-balances";
    }

    @GetMapping("/{id}/delete")
    public String deleteLeaveBalance(@PathVariable Integer id) {
        leaveBalanceService.deleteLeaveBalance(id);
        return "redirect:/leave-balances";
    }
}