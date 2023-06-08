package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, String> {


    @Modifying
    @Query("UPDATE Client c SET c.verifiedBy = :agentOrAdmin, c.password = :password, c.enabled = true WHERE c.username = :username")
    void setVerified(
            @Param("username") String username,
            @Param("agentOrAdmin") User agentOrAdmin,
            @Param("password") String generatedPassword
    );

    Optional<Client> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
