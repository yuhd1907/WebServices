package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.AssessmentResult;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    boolean existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(Long assignmentId, Long roundId, Long criterionId);
    List<AssessmentResult> findByEvaluatedBy_Username(String username);
    List<AssessmentResult> findByAssignment_Student_User_Username(String username);
    Optional<AssessmentResult> findByResultIdAndEvaluatedBy_Username(Long resultId, String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM AssessmentResult r WHERE r.assignment.assignmentId IN :assignmentIds")
    void deleteByAssignment_AssignmentIdIn(@Param("assignmentIds") List<Long> assignmentIds);

    // 4. Lấy danh sách kết quả đánh giá chi tiết (lọc theo quyền, username, assignment_id)
    @Query("SELECT r FROM AssessmentResult r WHERE " +
           "(:assignmentId IS NULL OR r.assignment.assignmentId = :assignmentId) AND (" +
           "(:role = 'ROLE_ADMIN') OR " +
           "(:role = 'ROLE_MENTOR' AND r.evaluatedBy.username = :username) OR " +
           "(:role = 'ROLE_STUDENT' AND r.assignment.student.user.username = :username))")
    List<AssessmentResult> findAllByRoleAndUsernameAndAssignmentId(
            @Param("role") String role,
            @Param("username") String username,
            @Param("assignmentId") Long assignmentId);
}
