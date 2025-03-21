package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.CompetencyDao;

@Service
public class CompetencyService {
    private final CompetencyDao competencyDao;

    public CompetencyService(CompetencyDao competencyDao) {
        this.competencyDao = competencyDao;
    }
}
