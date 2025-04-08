package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Position;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.PositionService;

@Controller
@RequestMapping("/admin/pages/positions")
public class PositionController {

    private final PositionService positionService;
    private final DepartmentService departmentService;

    public PositionController(PositionService positionService, DepartmentService departmentService) {
        this.positionService = positionService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String getAllPositions(Model model) {
        model.addAttribute("positions", positionService.getAllPositions());
        return "pages/position/list";
    }

    @GetMapping("/{id}")
    public String getPositionById(@PathVariable Integer id, Model model) {
        model.addAttribute("position", positionService.getPositionById(id));
        return "pages/position/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("position", new Position());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "pages/position/create";
    }

    @PostMapping
    public String createPosition(@ModelAttribute Position position) {
        positionService.createPosition(position);
        return "redirect:/admin/pages/positions";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("position", positionService.getPositionById(id));
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "pages/position/edit";
    }

    @PostMapping("/{id}")
    public String updatePosition(@PathVariable Integer id, @ModelAttribute Position position) {
        position.setId(id);
        positionService.updatePosition(position);
        return "redirect:/admin/pages/positions";
    }

    @GetMapping("/{id}/delete")
    public String deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
        return "redirect:/admin/pages/positions";
    }
}