package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.ApplicantDao;

@Service
public class ApplicantService {
    private final ApplicantDao applicantDao;

    public ApplicantService(ApplicantDao applicantDao) {
        this.applicantDao = applicantDao;
    }


}
