package vn.ptit.hrms.controller.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.ptit.hrms.domain.warehouse.DimPosition;
import vn.ptit.hrms.service.warehouse.DimPositionService;

import java.util.List;

@Controller
@RequestMapping("admin/pages/dim-position")
public class DimPositionController {

    private final DimPositionService dimPositionService;

    public DimPositionController(DimPositionService dimPositionService) {
        this.dimPositionService = dimPositionService;
    }

    @GetMapping("/list")
    public String getAllCurrentDimPositions(Model model) {
        List<DimPosition> dimPositions = dimPositionService.getAllCurrentDimPositions();
        model.addAttribute("dimPositions", dimPositions);
        return "pages/dim_position/list";
    }

    @GetMapping("/{positionSk}")
    public String getDimPositionByPositionSk(@PathVariable Integer positionSk, Model model) {
        DimPosition dimPosition = dimPositionService.getDimPositionByPositionSk(positionSk);
        model.addAttribute("dimPosition", dimPosition);
        return "pages/dim_position/view";
    }

    @GetMapping("/position-id/{positionId}")
    public String getDimPositionByPositionId(@PathVariable Integer positionId, Model model) {
        DimPosition dimPosition = dimPositionService.getDimPositionByPositionId(positionId);
        model.addAttribute("dimPosition", dimPosition);
        return "pages/dim_position/view";
    }

    @GetMapping("/department/{departmentSk}")
    public String getDimPositionsByDepartmentSk(@PathVariable Integer departmentSk, Model model) {
        List<DimPosition> dimPositions = dimPositionService.getDimPositionsByDepartmentSk(departmentSk);
        model.addAttribute("dimPositions", dimPositions);
        model.addAttribute("filterCriteria", "Department SK: " + departmentSk);
        return "pages/dim_position/list";
    }
}