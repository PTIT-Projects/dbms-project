package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.InsuranceDao;

@Service
public class InsuranceService {
    private final InsuranceDao insuranceDao;

    public InsuranceService(InsuranceDao insuranceDao) {
        this.insuranceDao = insuranceDao;
    }
}
