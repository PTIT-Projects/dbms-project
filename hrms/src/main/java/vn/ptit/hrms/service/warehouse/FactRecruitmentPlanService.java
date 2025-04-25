package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.FactRecruitmentPlanDao;
import vn.ptit.hrms.domain.warehouse.FactRecruitmentPlan;

import java.util.List;

@Service
public class FactRecruitmentPlanService {

    private final FactRecruitmentPlanDao factRecruitmentPlanDao;

    public FactRecruitmentPlanService(FactRecruitmentPlanDao factRecruitmentPlanDao) {
        this.factRecruitmentPlanDao = factRecruitmentPlanDao;
    }

    public List<FactRecruitmentPlan> getAllRecruitmentPlans() {
        return factRecruitmentPlanDao.getRecruitmentPlans();
    }

    public List<FactRecruitmentPlan> getRecruitmentPlansByDepartmentSk(int departmentSk) {
        return factRecruitmentPlanDao.getRecruitmentPlansByDepartment(departmentSk);
    }

    public List<FactRecruitmentPlan> getRecruitmentPlansByPositionSk(int positionSk) {
        return factRecruitmentPlanDao.getRecruitmentPlansByPosition(positionSk);
    }

    // You can add service methods corresponding to other DAO methods if needed
    /*
    public List<FactRecruitmentPlan> getRecruitmentPlansByDateRange(int startDateSk, int endDateSk) {
        return factRecruitmentPlanDao.getRecruitmentPlansByDateRange(startDateSk, endDateSk);
    }
    */
}
