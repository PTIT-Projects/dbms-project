package vn.ptit.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.ptit.hrms.dao.ApplicantDao;

@Controller
public class AdminController {

    private final ApplicantDao applicantDao;

    public AdminController(ApplicantDao applicantDao) {
        this.applicantDao = applicantDao;
    }

    @GetMapping({"/", "/admin", "/admin/index", "index.html"})
    public String dashboard() {
        return "index";
    }

    @GetMapping("/pages/{category}/{page}")
    public String viewPagesDirectly(@PathVariable String category, @PathVariable String page) {
        return "pages/" + category + "/" + page;
    }

    @GetMapping("/admin/pages/{category}/{page}")
    public String viewAdminPages(@PathVariable String category, @PathVariable String page) {
        return "pages/" + category + "/" + page;
    }
    @GetMapping("/test")
    public void test() {
        System.out.println(applicantDao.getAllApplicants());
    }
}