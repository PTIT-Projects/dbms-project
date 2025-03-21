package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.service.ApplicantService;
import vn.ptit.hrms.service.RecruitmentPlanService;

@Controller
@RequestMapping("/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;
    private final RecruitmentPlanService recruitmentPlanService;

    public ApplicantController(ApplicantService applicantService, RecruitmentPlanService recruitmentPlanService) {
        this.applicantService = applicantService;
        this.recruitmentPlanService = recruitmentPlanService;
    }

    @GetMapping
    public String getAllApplicants(Model model) {
        model.addAttribute("applicants", applicantService.getAllApplicants());
        return "pages/recruitment/applicant/list";
    }

    @GetMapping("/{id}")
    public String getApplicantById(@PathVariable Integer id, Model model) {
        model.addAttribute("applicant", applicantService.getApplicantById(id));
        return "pages/recruitment/applicant/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("applicant", new Applicant());
        model.addAttribute("recruitmentPlans", recruitmentPlanService.getAllRecruitmentPlans());
        return "pages/recruitment/applicant/create";
    }

    @PostMapping
    public String createApplicant(@ModelAttribute Applicant applicant) {
        applicantService.createApplicant(applicant);
        return "redirect:/applicants";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("applicant", applicantService.getApplicantById(id));
        model.addAttribute("recruitmentPlans", recruitmentPlanService.getAllRecruitmentPlans());
        return "pages/recruitment/applicant/edit";
    }

    @PostMapping("/{id}")
    public String updateApplicant(@PathVariable Integer id, @ModelAttribute Applicant applicant) {
        applicant.setId(id);
        applicantService.updateApplicant(applicant);
        return "redirect:/applicants";
    }

    @GetMapping("/{id}/delete")
    public String deleteApplicant(@PathVariable Integer id) {
        applicantService.deleteApplicant(id);
        return "redirect:/applicants";
    }

    @GetMapping("/{id}/interview")
    public String moveToInterviewed(@PathVariable Integer id) {
        applicantService.moveApplicantToInterviewed(id);
        return "redirect:/applicants";
    }

    @GetMapping("/{id}/hire")
    public String hireApplicant(@PathVariable Integer id) {
        applicantService.hireApplicant(id);
        return "redirect:/applicants";
    }

    @GetMapping("/{id}/reject")
    public String rejectApplicant(@PathVariable Integer id) {
        applicantService.rejectApplicant(id);
        return "redirect:/applicants";
    }
}