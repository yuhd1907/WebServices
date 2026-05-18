package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.EvaluationCriterion;

@Repository
public interface EvaluationCriterionRepository extends JpaRepository<EvaluationCriterion, Long> {
    boolean existsByCriterionName(String criterionName);
}
