package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.PositionDao;
import vn.ptit.hrms.domain.Position;

import java.util.List;

@Service
public class PositionService {
    public final PositionDao positionDao;

    public PositionService(PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public void createPosition(Position position) {
        positionDao.createPosition(position);
    }

    public Position getPositionById(Integer id) {
        return positionDao.getPositionById(id);
    }

    public List<Position> getAllPositions() {
        return positionDao.getAllPositions();
    }

    public List<Position> getPositionsByDepartment(Integer departmentId) {
        List<Position> allPositions = positionDao.getAllPositions();
        return allPositions.stream()
                .filter(pos -> pos.getDepartment() != null &&
                        pos.getDepartment().getId().equals(departmentId))
                .toList();
    }

    public void updatePosition(Position position) {
        positionDao.updatePosition(position);
    }

    public void deletePosition(Integer id) {
        positionDao.deletePosition(id);
    }
}