package vn.ptit.hrms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.primary.Contract;
import vn.ptit.hrms.service.ContractService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/admin/pages/contracts")
public class ContractController {

    private final ContractService contractService;
    private final EmployeeService employeeService;

    public ContractController(ContractService contractService, EmployeeService employeeService) {
        this.contractService = contractService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllContracts(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String employeeSearch,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> contractPage = contractService.findContractPage(
                pageable, employeeSearch, contractType, status);

        model.addAttribute("contracts", contractPage.getContent());
        model.addAttribute("currentPage", contractPage.getNumber());
        model.addAttribute("totalPages", contractPage.getTotalPages());
        model.addAttribute("totalItems", contractPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("employeeSearch", employeeSearch);
        model.addAttribute("contractType", contractType);
        model.addAttribute("status", status);

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
        return "redirect:/admin/pages/contracts";
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
        return "redirect:/admin/pages/contracts";
    }

    @GetMapping("/{id}/delete")
    public String deleteContract(@PathVariable Integer id) {
        contractService.deleteContract(id);
        return "redirect:/admin/pages/contracts";
    }
}