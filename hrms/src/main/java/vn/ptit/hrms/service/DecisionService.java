package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.DecisionDao;

@Service
public class DecisionService {
    private final DecisionDao decisionDao;

    public DecisionService(DecisionDao decisionDao) {
        this.decisionDao = decisionDao;
    }
}
