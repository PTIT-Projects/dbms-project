package vn.ptit.hrms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.service.ApplicantService;
import vn.ptit.hrms.service.RecruitmentPlanService;


@Controller
@RequestMapping("/admin/pages/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;
    private final RecruitmentPlanService recruitmentPlanService;

    public ApplicantController(ApplicantService applicantService, RecruitmentPlanService recruitmentPlanService) {
        this.applicantService = applicantService;
        this.recruitmentPlanService = recruitmentPlanService;
    }

    @GetMapping
    public String getAllApplicants(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer planId,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Applicant> applicantPage = applicantService.applicantDao.findApplicantPage(pageable, search, planId, status);
        model.addAttribute("applicants", applicantPage.getContent());
        model.addAttribute("currentPage", applicantPage.getNumber());
        model.addAttribute("totalPages", applicantPage.getTotalPages());
        model.addAttribute("totalItems", applicantPage.getTotalElements());
        model.addAttribute("recruitmentPlans", this.recruitmentPlanService.getAllRecruitmentPlans());
        model.addAttribute("planId", planId);
        model.addAttribute("search", search);
        return "pages/applicant/list";
    }

    @GetMapping("/{id}")
    public String getApplicantById(@PathVariable Integer id, Model model) {
        model.addAttribute("applicant", applicantService.getApplicantById(id));
        return "pages/applicant/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("applicant", new Applicant());
        model.addAttribute("recruitmentPlans", recruitmentPlanService.getAllRecruitmentPlans());
        return "pages/applicant/create";
    }

    @PostMapping
    public String createApplicant(@ModelAttribute Applicant applicant) {
        applicantService.createApplicant(applicant);
        return "redirect:/admin/pages/applicants";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("applicant", applicantService.getApplicantById(id));
        model.addAttribute("recruitmentPlans", recruitmentPlanService.getAllRecruitmentPlans());
        return "pages/applicant/edit";
    }

    @PostMapping("/{id}")
    public String updateApplicant(@PathVariable Integer id, @ModelAttribute Applicant applicant) {
        applicant.setId(id);
        applicantService.updateApplicant(applicant);
        return "redirect:/admin/pages/applicants";
    }

    @GetMapping("/{id}/delete")
    public String deleteApplicant(@PathVariable Integer id) {
        applicantService.deleteApplicant(id);
        return "redirect:/admin/pages/applicants";
    }

    @GetMapping("/{id}/interview")
    public String moveToInterviewed(@PathVariable Integer id) {
        applicantService.moveApplicantToInterviewed(id);
        return "redirect:/admin/pages/applicants";
    }

    @GetMapping("/{id}/hire")
    public String hireApplicant(@PathVariable Integer id) {
        applicantService.hireApplicant(id);
        return "redirect:/admin/pages/applicants";
    }

    @GetMapping("/{id}/reject")
    public String rejectApplicant(@PathVariable Integer id) {
        applicantService.rejectApplicant(id);
        return "redirect:/admin/pages/applicants";
    }
}