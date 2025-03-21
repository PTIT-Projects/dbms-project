package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.LeaveBalanceDao;

@Service
public class LeaveBalanceService {
    private final LeaveBalanceDao leaveBalanceDao;

    public LeaveBalanceService(LeaveBalanceDao leaveBalanceDao) {
        this.leaveBalanceDao = leaveBalanceDao;
    }
}
