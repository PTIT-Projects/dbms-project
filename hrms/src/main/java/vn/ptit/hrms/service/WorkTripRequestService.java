package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.WorkTripRequestDao;

@Service
public class WorkTripRequestService {
    private final WorkTripRequestDao workTripRequestDao;

    public WorkTripRequestService(WorkTripRequestDao workTripRequestDao) {
        this.workTripRequestDao = workTripRequestDao;
    }
}
