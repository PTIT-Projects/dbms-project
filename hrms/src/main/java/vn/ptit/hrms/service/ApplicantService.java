package vn.ptit.hrms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.ApplicantDao;
import vn.ptit.hrms.domain.Applicant;
import vn.ptit.hrms.constant.ApplicantStatusEnum;

import java.util.List;

@Service
public class ApplicantService {
    public final ApplicantDao applicantDao;

    public ApplicantService(ApplicantDao applicantDao) {
        this.applicantDao = applicantDao;
    }

    public void createApplicant(Applicant applicant) {
        if (applicant.getStatus() == null) {
            applicant.setStatus(ApplicantStatusEnum.APPLIED);
        }
        applicantDao.createApplicant(applicant);
    }

    public Applicant getApplicantById(Integer id) {
        return applicantDao.getApplicantById(id);
    }

    public List<Applicant> getAllApplicants() {
        return applicantDao.getAllApplicants();
    }

    public List<Applicant> getApplicantsByStatus(ApplicantStatusEnum status) {
        List<Applicant> allApplicants = applicantDao.getAllApplicants();
        return allApplicants.stream()
                .filter(app -> app.getStatus() == status)
                .toList();
    }

    public List<Applicant> getApplicantsByRecruitmentPlan(Integer planId) {
        List<Applicant> allApplicants = applicantDao.getAllApplicants();
        return allApplicants.stream()
                .filter(app -> app.getPlan() != null &&
                        app.getPlan().getId().equals(planId))
                .toList();
    }

    public void moveApplicantToInterviewed(Integer id) {
        Applicant applicant = applicantDao.getApplicantById(id);
        if (applicant != null && applicant.getStatus() == ApplicantStatusEnum.APPLIED) {
            applicant.setStatus(ApplicantStatusEnum.INTERVIEWED);
            applicantDao.updateApplicant(applicant);
        }
    }

    public void hireApplicant(Integer id) {
        Applicant applicant = applicantDao.getApplicantById(id);
        if (applicant != null &&
                (applicant.getStatus() == ApplicantStatusEnum.APPLIED ||
                        applicant.getStatus() == ApplicantStatusEnum.INTERVIEWED)) {
            applicant.setStatus(ApplicantStatusEnum.HIRED);
            applicantDao.updateApplicant(applicant);
        }
    }

    public void rejectApplicant(Integer id) {
        Applicant applicant = applicantDao.getApplicantById(id);
        if (applicant != null &&
                (applicant.getStatus() == ApplicantStatusEnum.APPLIED ||
                        applicant.getStatus() == ApplicantStatusEnum.INTERVIEWED)) {
            applicant.setStatus(ApplicantStatusEnum.REJECTED);
            applicantDao.updateApplicant(applicant);
        }
    }

    public void updateApplicant(Applicant applicant) {
        applicantDao.updateApplicant(applicant);
    }

    public void deleteApplicant(Integer id) {
        applicantDao.deleteApplicant(id);
    }
    public Page<Applicant> getApplicantPage(Pageable pageable, String search, Integer recruitmentPlanId, String status) {
        return applicantDao.findApplicantPage(pageable, search, recruitmentPlanId, status);
    }

}