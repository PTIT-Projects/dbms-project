package vn.ptit.hrms.service;

import org.springframework.stereotype.Service;
import vn.ptit.hrms.dao.TrainingCourseDao;

@Service
public class TrainingCourseService {
    private final TrainingCourseDao trainingCourseDao;

    public TrainingCourseService(TrainingCourseDao trainingCourseDao) {
        this.trainingCourseDao = trainingCourseDao;
    }
}
