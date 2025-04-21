package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.primary.DecisionDao;
import vn.ptit.hrms.domain.primary.Decision;
import vn.ptit.hrms.constant.DecisionTypeEnum;

import java.time.LocalDate;
import java.util.List;

@Service
public class DecisionService {
    public final DecisionDao decisionDao;

    public DecisionService(DecisionDao decisionDao) {
        this.decisionDao = decisionDao;
    }

    public void createDecision(Decision decision) {
        decisionDao.createDecision(decision);
    }

    public Decision getDecisionById(Integer id) {
        return decisionDao.getDecisionById(id);
    }

    public List<Decision> getAllDecisions() {
        return decisionDao.getAllDecisions();
    }

    public List<Decision> getDecisionsByEmployee(Integer employeeId) {
        List<Decision> allDecisions = decisionDao.getAllDecisions();
        return allDecisions.stream()
                .filter(decision -> decision.getEmployee() != null &&
                        decision.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<Decision> getDecisionsByType(DecisionTypeEnum type) {
        List<Decision> allDecisions = decisionDao.getAllDecisions();
        return allDecisions.stream()
                .filter(decision -> decision.getDecisionType() == type)
                .toList();
    }

    public List<Decision> getDecisionsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Decision> allDecisions = decisionDao.getAllDecisions();
        return allDecisions.stream()
                .filter(decision -> decision.getDecisionDate() != null &&
                        !decision.getDecisionDate().isBefore(startDate) &&
                        !decision.getDecisionDate().isAfter(endDate))
                .toList();
    }

    public void updateDecision(Decision decision) {
        decisionDao.updateDecision(decision);
    }

    public void deleteDecision(Integer id) {
        decisionDao.deleteDecision(id);
    }
}