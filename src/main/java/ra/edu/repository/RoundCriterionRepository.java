package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.RoundCriterion;

import java.util.List;

@Repository
public interface RoundCriterionRepository extends JpaRepository<RoundCriterion, Long> {
    List<RoundCriterion> findByAssessmentRound_RoundId(Long roundId);
}
