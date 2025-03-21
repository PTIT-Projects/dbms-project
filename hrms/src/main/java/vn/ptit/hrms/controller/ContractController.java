package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Contract;
import vn.ptit.hrms.service.ContractService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;
    private final EmployeeService employeeService;

    public ContractController(ContractService contractService, EmployeeService employeeService) {
        this.contractService = contractService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllContracts(Model model) {
        model.addAttribute("contracts", contractService.getAllContracts());
        return "pages/contract/list";
    }

    @GetMapping("/{id}")
    public String getContractById(@PathVariable Integer id, Model model) {
        model.addAttribute("contract", contractService.getContractById(id));
        return "pages/contract/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("contract", new Contract());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/contract/create";
    }

    @PostMapping
    public String createContract(@ModelAttribute Contract contract) {
        contractService.createContract(contract);
        return "redirect:/contracts";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("contract", contractService.getContractById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/contract/edit";
    }

    @PostMapping("/{id}")
    public String updateContract(@PathVariable Integer id, @ModelAttribute Contract contract) {
        contract.setId(id);
        contractService.updateContract(contract);
        return "redirect:/contracts";
    }

    @GetMapping("/{id}/delete")
    public String deleteContract(@PathVariable Integer id) {
        contractService.deleteContract(id);
        return "redirect:/contracts";
    }
}