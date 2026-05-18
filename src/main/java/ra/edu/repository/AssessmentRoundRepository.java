package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.AssessmentRound;

import java.util.List;

@Repository
public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Long> {
    List<AssessmentRound> findByPhase_PhaseId(Long phaseId);
}
