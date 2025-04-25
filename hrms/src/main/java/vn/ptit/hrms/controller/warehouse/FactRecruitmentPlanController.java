package vn.ptit.hrms.controller.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.ptit.hrms.domain.warehouse.FactRecruitmentPlan;
import vn.ptit.hrms.service.warehouse.FactRecruitmentPlanService;

import java.util.List;

@Controller
@RequestMapping("admin/pages/recruitment-plan")
public class FactRecruitmentPlanController {

    private final FactRecruitmentPlanService factRecruitmentPlanService;

    public FactRecruitmentPlanController(FactRecruitmentPlanService factRecruitmentPlanService) {
        this.factRecruitmentPlanService = factRecruitmentPlanService;
    }

    @GetMapping("/list")
    public String getAllRecruitmentPlans(Model model) {
        List<FactRecruitmentPlan> recruitmentPlans = factRecruitmentPlanService.getAllRecruitmentPlans();
        model.addAttribute("recruitmentPlans", recruitmentPlans);
        return "pages/recruitment_plan/list";
    }

    @GetMapping("/department/{departmentSk}")
    public String getRecruitmentPlansByDepartmentSk(@PathVariable int departmentSk, Model model) {
        List<FactRecruitmentPlan> recruitmentPlans = factRecruitmentPlanService.getRecruitmentPlansByDepartmentSk(departmentSk);
        model.addAttribute("recruitmentPlans", recruitmentPlans);
        model.addAttribute("filterCriteria", "Department SK: " + departmentSk);
        return "pages/recruitment_plan/list";
    }

    @GetMapping("/position/{positionSk}")
    public String getRecruitmentPlansByPositionSk(@PathVariable int positionSk, Model model) {
        List<FactRecruitmentPlan> recruitmentPlans = factRecruitmentPlanService.getRecruitmentPlansByPositionSk(positionSk);
        model.addAttribute("recruitmentPlans", recruitmentPlans);
        model.addAttribute("filterCriteria", "Position SK: " + positionSk);
        return "pages/recruitment_plan/list";
    }

    // You can add controller methods corresponding to other service methods if needed
    /*
    @GetMapping("/date-range")
    public String getRecruitmentPlansByDateRange(@RequestParam int startDateSk, @RequestParam int endDateSk, Model model) {
        List<FactRecruitmentPlan> recruitmentPlans = factRecruitmentPlanService.getRecruitmentPlansByDateRange(startDateSk, endDateSk);
        model.addAttribute("recruitmentPlans", recruitmentPlans);
        model.addAttribute("filterCriteria", "Date Range: Start SK=" + startDateSk + ", End SK=" + endDateSk);
        return "pages/recruitment_plan/list";
    }
    */
}
