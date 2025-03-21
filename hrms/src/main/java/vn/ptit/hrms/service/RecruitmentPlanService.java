package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.RecruitmentPlanDao;

@Service
public class RecruitmentPlanService {
    private final RecruitmentPlanDao recruitmentPlanDao;

    public RecruitmentPlanService(RecruitmentPlanDao recruitmentPlanDao) {
        this.recruitmentPlanDao = recruitmentPlanDao;
    }
}
