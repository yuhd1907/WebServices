package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.AssessmentRound;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Long> {
    List<AssessmentRound> findByPhase_PhaseId(Long phaseId);

    // 1. Lấy danh sách tất cả các đợt đánh giá (có thể lọc theo phase_id)
    @Query("SELECT r FROM AssessmentRound r WHERE :phaseId IS NULL OR r.phase.phaseId = :phaseId")
    List<AssessmentRound> findAllWithFilter(@Param("phaseId") Long phaseId);
}
