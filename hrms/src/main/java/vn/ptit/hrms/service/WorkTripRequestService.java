package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.WorkTripRequestDao;
import vn.ptit.hrms.domain.WorkTripRequest;
import vn.ptit.hrms.constant.RegistrationStatusEnum;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkTripRequestService {
    public final WorkTripRequestDao workTripRequestDao;

    public WorkTripRequestService(WorkTripRequestDao workTripRequestDao) {
        this.workTripRequestDao = workTripRequestDao;
    }

    public void createWorkTripRequest(WorkTripRequest workTripRequest) {
        if (workTripRequest.getStatus() == null) {
            workTripRequest.setStatus(RegistrationStatusEnum.PENDING);
        }
        workTripRequestDao.createWorkTripRequest(workTripRequest);
    }

    public WorkTripRequest getWorkTripRequestById(Integer id) {
        return workTripRequestDao.getWorkTripRequestById(id);
    }

    public List<WorkTripRequest> getAllWorkTripRequests() {
        return workTripRequestDao.getAllWorkTripRequests();
    }

    public List<WorkTripRequest> getWorkTripRequestsByEmployee(Integer employeeId) {
        List<WorkTripRequest> allRequests = workTripRequestDao.getAllWorkTripRequests();
        return allRequests.stream()
                .filter(req -> req.getEmployee() != null &&
                        req.getEmployee().getId().equals(employeeId))
                .toList();
    }

    public List<WorkTripRequest> getWorkTripRequestsByStatus(RegistrationStatusEnum status) {
        List<WorkTripRequest> allRequests = workTripRequestDao.getAllWorkTripRequests();
        return allRequests.stream()
                .filter(req -> req.getStatus() == status)
                .toList();
    }

    public List<WorkTripRequest> getUpcomingWorkTrips() {
        LocalDate now = LocalDate.now();
        List<WorkTripRequest> allRequests = workTripRequestDao.getAllWorkTripRequests();

        return allRequests.stream()
                .filter(req -> req.getStatus() == RegistrationStatusEnum.APPROVED &&
                        req.getStartDate() != null &&
                        !req.getStartDate().isBefore(now))
                .toList();
    }

    public List<WorkTripRequest> getCurrentWorkTrips() {
        LocalDate now = LocalDate.now();
        List<WorkTripRequest> allRequests = workTripRequestDao.getAllWorkTripRequests();

        return allRequests.stream()
                .filter(req -> req.getStatus() == RegistrationStatusEnum.APPROVED &&
                        req.getStartDate() != null &&
                        req.getEndDate() != null &&
                        !req.getStartDate().isAfter(now) &&
                        !req.getEndDate().isBefore(now))
                .toList();
    }

    public void approveWorkTripRequest(Integer id) {
        WorkTripRequest req = workTripRequestDao.getWorkTripRequestById(id);
        if (req != null && req.getStatus() == RegistrationStatusEnum.PENDING) {
            req.setStatus(RegistrationStatusEnum.APPROVED);
            workTripRequestDao.updateWorkTripRequest(req);
        }
    }

    public void rejectWorkTripRequest(Integer id) {
        WorkTripRequest req = workTripRequestDao.getWorkTripRequestById(id);
        if (req != null && req.getStatus() == RegistrationStatusEnum.PENDING) {
            req.setStatus(RegistrationStatusEnum.REJECTED);
            workTripRequestDao.updateWorkTripRequest(req);
        }
    }

    public void updateWorkTripRequest(WorkTripRequest workTripRequest) {
        workTripRequestDao.updateWorkTripRequest(workTripRequest);
    }

    public void deleteWorkTripRequest(Integer id) {
        workTripRequestDao.deleteWorkTripRequest(id);
    }
}