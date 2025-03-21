package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.DependentDeductionDao;

@Service
public class DependentDeductionService {
    private final DependentDeductionDao dependentDeductionDao;

    public DependentDeductionService(DependentDeductionDao dependentDeductionDao) {
        this.dependentDeductionDao = dependentDeductionDao;
    }
}
