package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // phương thức đăng nhập theo username
    @Query("FROM User WHERE username = :username OR email= :username OR phoneNumber = :username")
    Optional<User> loadUserByUsername(@Param("username") String username);
}
