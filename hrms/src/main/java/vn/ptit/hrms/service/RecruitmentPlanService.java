package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.RecruitmentPlanDao;
import vn.ptit.hrms.domain.RecruitmentPlan;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecruitmentPlanService {
    public final RecruitmentPlanDao recruitmentPlanDao;

    public RecruitmentPlanService(RecruitmentPlanDao recruitmentPlanDao) {
        this.recruitmentPlanDao = recruitmentPlanDao;
    }

    public void createRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        recruitmentPlanDao.createRecruitmentPlan(recruitmentPlan);
    }

    public RecruitmentPlan getRecruitmentPlanById(Integer id) {
        return recruitmentPlanDao.getRecruitmentPlanById(id);
    }

    public List<RecruitmentPlan> getAllRecruitmentPlans() {
        return recruitmentPlanDao.getAllRecruitmentPlans();
    }

    public List<RecruitmentPlan> getActiveRecruitmentPlans() {
        LocalDate now = LocalDate.now();
        List<RecruitmentPlan> allPlans = recruitmentPlanDao.getAllRecruitmentPlans();

        return allPlans.stream()
                .filter(plan -> plan.getEndDate() != null &&
                        !plan.getEndDate().isBefore(now))
                .toList();
    }

    public List<RecruitmentPlan> getRecruitmentPlansByDepartment(Integer departmentId) {
        List<RecruitmentPlan> allPlans = recruitmentPlanDao.getAllRecruitmentPlans();
        return allPlans.stream()
                .filter(plan -> plan.getDepartment() != null &&
                        plan.getDepartment().getId().equals(departmentId))
                .toList();
    }

    public void updateRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        recruitmentPlanDao.updateRecruitmentPlan(recruitmentPlan);
    }

    public void deleteRecruitmentPlan(Integer id) {
        recruitmentPlanDao.deleteRecruitmentPlan(id);
    }
}