package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.primary.RegistrationDao;
import vn.ptit.hrms.domain.primary.Registration;
import vn.ptit.hrms.constant.RegistrationStatusEnum;
import vn.ptit.hrms.constant.RegistrationTypeEnum;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistrationService {
    public final RegistrationDao registrationDao;
    private final LeaveBalanceService leaveBalanceService;

    public RegistrationService(RegistrationDao registrationDao, LeaveBalanceService leaveBalanceService) {
        this.registrationDao = registrationDao;
        this.leaveBalanceService = leaveBalanceService;
    }

    public void createRegistration(Registration registration) {
        if (registration.getRequestDate() == null) {
            registration.setRequestDate(LocalDate.now());
        }

        if (registration.getStatus() == null) {
            registration.setStatus(RegistrationStatusEnum.PENDING);
        }

        // Check leave balance if registration type is LEAVE
        if (registration.getRegistrationType() == RegistrationTypeEnum.LEAVE) {
            // Assuming the registration details field contains the number of days requested
            // You may want to create a more structured approach
            try {
                int daysRequested = Integer.parseInt(registration.getDetails().trim());
                if (!leaveBalanceService.hasAvailableLeaveBalance(
                        registration.getEmployee().getId(), daysRequested)) {
                    throw new IllegalStateException("Insufficient leave balance");
                }
            } catch (NumberFormatException e) {
                // Handle invalid format in details field
                throw new IllegalArgumentException("Invalid leave days format in details field");
            }
        }

        registrationDao.createRegistration(registration);
    }

    public Registration getRegistrationById(Integer id) {
        return registrationDao.getRegistrationById(id);
    }

    public List<Registration> getAllRegistrations() {
        return registrationDao.getAllRegistrations();
    }

    public List<Registration> getRegistrationsByEmployee(Integer employeeId) {
        List<Registration> allRegistrations = registrationDao.getAllRegistrations();
        return allRegistrations.stream()
                .filter(reg -> reg.getEmployee() != null &&
                        reg.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<Registration> getRegistrationsByStatus(RegistrationStatusEnum status) {
        List<Registration> allRegistrations = registrationDao.getAllRegistrations();
        return allRegistrations.stream()
                .filter(reg -> reg.getStatus() == status)
                .toList();
    }

    public List<Registration> getRegistrationsByType(RegistrationTypeEnum type) {
        List<Registration> allRegistrations = registrationDao.getAllRegistrations();
        return allRegistrations.stream()
                .filter(reg -> reg.getRegistrationType() == type)
                .toList();
    }

    public List<Registration> getPendingRegistrationsForApprover(Integer approverId) {
        List<Registration> allRegistrations = registrationDao.getAllRegistrations();
        return allRegistrations.stream()
                .filter(reg -> reg.getStatus() == RegistrationStatusEnum.PENDING)
                .toList();
    }

    public void approveRegistration(Integer id, Integer approverId) {
        Registration reg = registrationDao.getRegistrationById(id);
        if (reg != null && reg.getStatus() == RegistrationStatusEnum.PENDING) {
            reg.setStatus(RegistrationStatusEnum.APPROVED);

            // Set approver
            // You would need to fetch the employee entity for the approver
            // This is just a placeholder

            // Handle leave balance deduction if this is a leave request
            if (reg.getRegistrationType() == RegistrationTypeEnum.LEAVE) {
                try {
                    int daysRequested = Integer.parseInt(reg.getDetails().trim());
                    leaveBalanceService.getCurrentYearLeaveBalance(reg.getEmployee().getId())
                            .ifPresent(balance ->
                                    leaveBalanceService.deductLeaveBalance(balance.getId(), daysRequested)
                            );
                } catch (NumberFormatException e) {
                    // Handle invalid format
                }
            }

            registrationDao.updateRegistration(reg);
        }
    }

    public void rejectRegistration(Integer id, Integer approverId) {
        Registration reg = registrationDao.getRegistrationById(id);
        if (reg != null && reg.getStatus() == RegistrationStatusEnum.PENDING) {
            reg.setStatus(RegistrationStatusEnum.REJECTED);

            // Set approver
            // You would need to fetch the employee entity for the approver

            registrationDao.updateRegistration(reg);
        }
    }

    public void updateRegistration(Registration registration) {
        registrationDao.updateRegistration(registration);
    }

    public void deleteRegistration(Integer id) {
        registrationDao.deleteRegistration(id);
    }
    public int getPendingRegistrationCount() {
        return this.registrationDao.countPendingRegistration();
    }
}