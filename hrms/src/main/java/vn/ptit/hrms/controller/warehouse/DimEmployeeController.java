package vn.ptit.hrms.controller.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ptit.hrms.domain.warehouse.DimEmployee;
import vn.ptit.hrms.service.warehouse.DimEmployeeService;

import java.util.List;

@Controller
@RequestMapping("admin/pages/dim-employee")
public class DimEmployeeController {

    private final DimEmployeeService dimEmployeeService;

    public DimEmployeeController(DimEmployeeService dimEmployeeService) {
        this.dimEmployeeService = dimEmployeeService;
    }

    @GetMapping("/list")
    public String getAllDimEmployees(Model model) {
        List<DimEmployee> dimEmployees = dimEmployeeService.getAllDimEmployees();
        model.addAttribute("dimEmployees", dimEmployees);
        return "pages/dim_employee/list";
    }

    @GetMapping("/department/{departmentSk}")
    public String getDimEmployeesByDepartmentSk(@PathVariable Integer departmentSk, Model model) {
        List<DimEmployee> dimEmployees = dimEmployeeService.getDimEmployeesByDepartmentSk(departmentSk);
        model.addAttribute("dimEmployees", dimEmployees);
        model.addAttribute("filterCriteria", "Department SK: " + departmentSk);
        return "pages/dim_employee/list";
    }

    @GetMapping("/department/name")
    public String getDimEmployeesByDepartmentName(@RequestParam String name, Model model) {
        List<DimEmployee> dimEmployees = dimEmployeeService.getDimEmployeesByDepartmentName(name);
        model.addAttribute("dimEmployees", dimEmployees);
        model.addAttribute("filterCriteria", "Department Name: " + name);
        return "pages/dim_employee/list";
    }

    @GetMapping("/years-worked")
    public String getDimEmployeesByTotalYearsWorked(@RequestParam double years, Model model) {
        List<DimEmployee> dimEmployees = dimEmployeeService.getDimEmployeesByTotalYearsWorked(years);
        model.addAttribute("dimEmployees", dimEmployees);
        model.addAttribute("filterCriteria", "Total Years Worked: " + years);
        return "pages/dim_employee/list";
    }

    @GetMapping("/position/{positionSk}")
    public String getDimEmployeesByPositionSk(@PathVariable Integer positionSk, Model model) {
        List<DimEmployee> dimEmployees = dimEmployeeService.getDimEmployeesByPositionSk(positionSk);
        model.addAttribute("dimEmployees", dimEmployees);
        model.addAttribute("filterCriteria", "Position SK: " + positionSk);
        return "pages/dim_employee/list";
    }

    @GetMapping("/position/name")
    public String getDimEmployeesByPositionName(@RequestParam String name, Model model) {
        List<DimEmployee> dimEmployees = dimEmployeeService.getDimEmployeesByPositionName(name);
        model.addAttribute("dimEmployees", dimEmployees);
        model.addAttribute("filterCriteria", "Position Name: " + name);
        return "pages/dim_employee/list";
    }
}
