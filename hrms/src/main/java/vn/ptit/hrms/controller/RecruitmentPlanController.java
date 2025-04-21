package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.RecruitmentPlan;
import vn.ptit.hrms.service.RecruitmentPlanService;
import vn.ptit.hrms.service.DepartmentService;
import vn.ptit.hrms.service.PositionService;

@Controller
@RequestMapping("/admin/pages/recruitment-plans")
public class RecruitmentPlanController {

    private final RecruitmentPlanService recruitmentPlanService;
    private final DepartmentService departmentService;
    private final PositionService positionService;

    public RecruitmentPlanController(RecruitmentPlanService recruitmentPlanService,
                                     DepartmentService departmentService,
                                     PositionService positionService) {
        this.recruitmentPlanService = recruitmentPlanService;
        this.departmentService = departmentService;
        this.positionService = positionService;
    }

    @GetMapping
    public String getAllRecruitmentPlans(Model model) {
        model.addAttribute("recruitmentPlans", recruitmentPlanService.getAllRecruitmentPlans());
        return "pages/recruitmentplan/list";
    }

    @GetMapping("/{id}")
    public String getRecruitmentPlanById(@PathVariable Integer id, Model model) {
        model.addAttribute("recruitmentPlan", recruitmentPlanService.getRecruitmentPlanById(id));
        return "pages/recruitmentplan/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("recruitmentPlan", new RecruitmentPlan());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", positionService.getAllPositions());
        return "pages/recruitmentplan/create";
    }

    @PostMapping
    public String createRecruitmentPlan(@ModelAttribute RecruitmentPlan recruitmentPlan) {
        recruitmentPlanService.createRecruitmentPlan(recruitmentPlan);
        return "redirect:/admin/pages/recruitment-plans";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("recruitmentPlan", recruitmentPlanService.getRecruitmentPlanById(id));
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("positions", positionService.getAllPositions());
        return "pages/recruitmentplan/edit";
    }

    @PostMapping("/{id}")
    public String updateRecruitmentPlan(@PathVariable Integer id, @ModelAttribute RecruitmentPlan recruitmentPlan) {
        recruitmentPlan.setId(id);
        recruitmentPlanService.updateRecruitmentPlan(recruitmentPlan);
        return "redirect:/admin/pages/recruitment-plans";
    }

    @GetMapping("/{id}/delete")
    public String deleteRecruitmentPlan(@PathVariable Integer id) {
        recruitmentPlanService.deleteRecruitmentPlan(id);
        return "redirect:/admin/pages/recruitment-plans";
    }
}