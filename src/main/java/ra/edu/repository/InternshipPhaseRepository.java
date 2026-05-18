package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.entity.InternshipPhase;

@Repository
public interface InternshipPhaseRepository extends JpaRepository<InternshipPhase, Long> {
    boolean existsByPhaseName(String phaseName);
}
