package vn.ptit.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ptit.hrms.domain.TrainingResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TrainingResultDao extends JpaRepository<TrainingResult, Integer> {
    List<TrainingResult> findByEmployeeId(Integer employeeId);
    List<TrainingResult> findByCourseId(Integer courseId);
    List<TrainingResult> findByCompletionStatus(String completionStatus);
}

@Repository
@Transactional
class TrainingResultDAOImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveTrainingResult(TrainingResult trainingResult) {
        entityManager.persist(trainingResult);
    }

    public TrainingResult updateTrainingResult(TrainingResult trainingResult) {
        return entityManager.merge(trainingResult);
    }

    public void deleteTrainingResult(Integer id) {
        TrainingResult trainingResult = entityManager.find(TrainingResult.class, id);
        if (trainingResult != null) {
            entityManager.remove(trainingResult);
        }
    }

    public TrainingResult findById(Integer id) {
        return entityManager.find(TrainingResult.class, id);
    }
}

