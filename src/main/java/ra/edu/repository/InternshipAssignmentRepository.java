package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.InternshipAssignment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {
    boolean existsByStudent_StudentIdAndPhase_PhaseId(Long studentId, Long phaseId);
    List<InternshipAssignment> findByMentor_User_Username(String username);
    List<InternshipAssignment> findByStudent_User_Username(String username);
    Optional<InternshipAssignment> findByAssignmentIdAndStudent_User_Username(Long assignmentId, String username);
    Optional<InternshipAssignment> findByAssignmentIdAndMentor_User_Username(Long assignmentId, String username);

    @Query("SELECT a.assignmentId FROM InternshipAssignment a WHERE a.student.studentId = :studentId")
    List<Long> findAssignmentIdsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT a.assignmentId FROM InternshipAssignment a WHERE a.mentor.mentorId = :mentorId")
    List<Long> findAssignmentIdsByMentorId(@Param("mentorId") Long mentorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM InternshipAssignment a WHERE a.student.studentId = :studentId")
    void deleteByStudent_StudentId(@Param("studentId") Long studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM InternshipAssignment a WHERE a.mentor.mentorId = :mentorId")
    void deleteByMentor_MentorId(@Param("mentorId") Long mentorId);

    // 2. Lấy danh sách phân công thực tập (lọc theo quyền và username)
    @Query("SELECT a FROM InternshipAssignment a WHERE " +
           "(:role = 'ROLE_ADMIN') OR " +
           "(:role = 'ROLE_MENTOR' AND a.mentor.user.username = :username) OR " +
           "(:role = 'ROLE_STUDENT' AND a.student.user.username = :username)")
    List<InternshipAssignment> findAllByRoleAndUsername(@Param("role") String role, @Param("username") String username);

    // 3. Lấy chi tiết một phân công thực tập theo ID (lọc theo quyền và username)
    @Query("SELECT a FROM InternshipAssignment a WHERE a.assignmentId = :assignmentId AND (" +
           "(:role = 'ROLE_ADMIN') OR " +
           "(:role = 'ROLE_MENTOR' AND a.mentor.user.username = :username) OR " +
           "(:role = 'ROLE_STUDENT' AND a.student.user.username = :username))")
    Optional<InternshipAssignment> findByIdAndRoleAndUsername(@Param("assignmentId") Long assignmentId, @Param("role") String role, @Param("username") String username);
}
