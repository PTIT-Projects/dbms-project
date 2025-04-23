package vn.ptit.hrms.service.warehouse;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.warehouse.DimPositionDao;
import vn.ptit.hrms.domain.warehouse.DimPosition;

import java.util.List;

@Service
public class DimPositionService {

    private final DimPositionDao dimPositionDao;

    public DimPositionService(DimPositionDao dimPositionDao) {
        this.dimPositionDao = dimPositionDao;
    }

    public DimPosition getDimPositionByPositionSk(Integer positionSk) {
        return dimPositionDao.getPositionByPositionSk(positionSk);
    }

    public DimPosition getDimPositionByPositionId(Integer positionId) {
        return dimPositionDao.getPositionByPositionId(positionId);
    }

    public List<DimPosition> getDimPositionsByDepartmentSk(Integer departmentSk) {
        return dimPositionDao.getPositionsByDepartmentSk(departmentSk);
    }

    public List<DimPosition> getAllCurrentDimPositions() {
        return dimPositionDao.getAllCurrentPositions();
    }
}