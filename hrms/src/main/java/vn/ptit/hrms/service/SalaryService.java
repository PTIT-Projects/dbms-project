package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.SalaryDao;

@Service
public class SalaryService {
    private final SalaryDao salaryDao;

    public SalaryService(SalaryDao salaryDao) {
        this.salaryDao = salaryDao;
    }
}
