package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.PositionDao;

@Service
public class PositionService {
    private final PositionDao positionDao;

    public PositionService(PositionDao positionDao) {
        this.positionDao = positionDao;
    }
}
