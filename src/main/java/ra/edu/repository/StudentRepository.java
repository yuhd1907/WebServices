package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
