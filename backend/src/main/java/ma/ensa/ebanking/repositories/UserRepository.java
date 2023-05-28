package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE User SET password = :password WHERE id = :id")
    void resetPassword(
            @Param("id")       String id,
            @Param("password") String password
    );

}
