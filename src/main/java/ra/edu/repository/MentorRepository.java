package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Mentor;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    // Tìm mentor theo user_id (do mentor_id = user_id)
    Optional<Mentor> findByUser_Username(String username);
}
