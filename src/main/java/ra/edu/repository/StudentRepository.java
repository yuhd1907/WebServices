package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentCode(String studentCode);
    java.util.Optional<Student> findByUser_UserId(Long userId);
    java.util.Optional<Student> findByUser_Username(String username);
}
