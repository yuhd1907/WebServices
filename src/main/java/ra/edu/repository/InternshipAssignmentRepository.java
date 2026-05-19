package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.InternshipAssignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {
    boolean existsByStudent_StudentIdAndPhase_PhaseId(Long studentId, Long phaseId);
    List<InternshipAssignment> findByMentor_User_Username(String username);
    List<InternshipAssignment> findByStudent_User_Username(String username);
    Optional<InternshipAssignment> findByAssignmentIdAndStudent_User_Username(Long assignmentId, String username);
    Optional<InternshipAssignment> findByAssignmentIdAndMentor_User_Username(Long assignmentId, String username);
}
