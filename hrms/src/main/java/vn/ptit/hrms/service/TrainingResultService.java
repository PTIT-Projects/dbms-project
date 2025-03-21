package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.TrainingResultDao;

@Service
public class TrainingResultService {
    private final TrainingResultDao trainingResultDao;

    public TrainingResultService(TrainingResultDao trainingResultDao) {
        this.trainingResultDao = trainingResultDao;
    }
}
