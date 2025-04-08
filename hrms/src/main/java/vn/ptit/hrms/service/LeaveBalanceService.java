package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.LeaveBalanceDao;
import vn.ptit.hrms.domain.LeaveBalance;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveBalanceService {
    public final LeaveBalanceDao leaveBalanceDao;

    public LeaveBalanceService(LeaveBalanceDao leaveBalanceDao) {
        this.leaveBalanceDao = leaveBalanceDao;
    }

    public void createLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalanceDao.createLeaveBalance(leaveBalance);
    }

    public LeaveBalance getLeaveBalanceById(Integer id) {
        return leaveBalanceDao.getLeaveBalanceById(id);
    }

    public List<LeaveBalance> getAllLeaveBalances() {
        return leaveBalanceDao.getAllLeaveBalances();
    }

    public List<LeaveBalance> getLeaveBalancesByEmployee(Integer employeeId) {
        List<LeaveBalance> allLeaveBalances = leaveBalanceDao.getAllLeaveBalances();
        return allLeaveBalances.stream()
                .filter(balance -> balance.getEmployee() != null &&
                        balance.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public Optional<LeaveBalance> getCurrentYearLeaveBalance(Integer employeeId) {
        int currentYear = java.time.Year.now().getValue();
        List<LeaveBalance> employeeBalances = getLeaveBalancesByEmployee(employeeId);

        return employeeBalances.stream()
                .filter(balance -> balance.getYear() == currentYear)
                .findFirst();
    }

    public boolean hasAvailableLeaveBalance(Integer employeeId, int requestedDays) {
        Optional<LeaveBalance> currentBalance = getCurrentYearLeaveBalance(employeeId);

        return currentBalance.isPresent() &&
                currentBalance.get().getRemainingLeaveDays() >= requestedDays;
    }

    public void deductLeaveBalance(Integer leaveBalanceId, int daysToDeduct) {
        LeaveBalance balance = leaveBalanceDao.getLeaveBalanceById(leaveBalanceId);
        if (balance != null) {
            int currentUsed = balance.getUsedLeaveDays();
            int currentRemaining = balance.getRemainingLeaveDays();

            balance.setUsedLeaveDays(currentUsed + daysToDeduct);
            balance.setRemainingLeaveDays(currentRemaining - daysToDeduct);

            leaveBalanceDao.updateLeaveBalance(balance);
        }
    }

    public void updateLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalanceDao.updateLeaveBalance(leaveBalance);
    }

    public void deleteLeaveBalance(Integer id) {
        leaveBalanceDao.deleteLeaveBalance(id);
    }
}