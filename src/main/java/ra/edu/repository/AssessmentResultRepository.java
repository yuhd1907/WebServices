package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.AssessmentResult;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    boolean existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(Long assignmentId, Long roundId, Long criterionId);
    List<AssessmentResult> findByEvaluatedBy_Username(String username);
    List<AssessmentResult> findByAssignment_Student_User_Username(String username);
    Optional<AssessmentResult> findByResultIdAndEvaluatedBy_Username(Long resultId, String username);
}
