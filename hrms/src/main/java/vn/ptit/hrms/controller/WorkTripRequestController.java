package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.primary.WorkTripRequest;
import vn.ptit.hrms.service.WorkTripRequestService;
import vn.ptit.hrms.service.EmployeeService;

@Controller
@RequestMapping("/admin/pages/work-trips")
public class WorkTripRequestController {

    private final WorkTripRequestService workTripRequestService;
    private final EmployeeService employeeService;

    public WorkTripRequestController(WorkTripRequestService workTripRequestService, EmployeeService employeeService) {
        this.workTripRequestService = workTripRequestService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllWorkTripRequests(Model model) {
        model.addAttribute("workTrips", workTripRequestService.getAllWorkTripRequests());
        return "pages/worktrip/list";
    }

    @GetMapping("/{id}")
    public String getWorkTripRequestById(@PathVariable Integer id, Model model) {
        model.addAttribute("workTrip", workTripRequestService.getWorkTripRequestById(id));
        return "pages/worktrip/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("workTrip", new WorkTripRequest());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/worktrip/create";
    }

    @PostMapping
    public String createWorkTripRequest(@ModelAttribute WorkTripRequest workTripRequest) {
        workTripRequestService.createWorkTripRequest(workTripRequest);
        return "redirect:/admin/pages/work-trips";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("workTrip", workTripRequestService.getWorkTripRequestById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "pages/worktrip/edit";
    }

    @PostMapping("/{id}")
    public String updateWorkTripRequest(@PathVariable Integer id, @ModelAttribute WorkTripRequest workTripRequest) {
        workTripRequest.setId(id);
        workTripRequestService.updateWorkTripRequest(workTripRequest);
        return "redirect:/admin/pages/work-trips";
    }

    @GetMapping("/{id}/delete")
    public String deleteWorkTripRequest(@PathVariable Integer id) {
        workTripRequestService.deleteWorkTripRequest(id);
        return "redirect:/admin/pages/work-trips";
    }

    @GetMapping("/{id}/approve")
    public String approveWorkTripRequest(@PathVariable Integer id) {
        workTripRequestService.approveWorkTripRequest(id);
        return "redirect:/admin/pages/work-trips";
    }

    @GetMapping("/{id}/reject")
    public String rejectWorkTripRequest(@PathVariable Integer id) {
        workTripRequestService.rejectWorkTripRequest(id);
        return "redirect:/admin/pages/work-trips";
    }
}