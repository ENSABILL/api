package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {


    @Modifying
    @Query("UPDATE Client c SET c.verifiedBy = :agent WHERE c.username = :username")
    void setVerified(
            @Param("username") String username,
            @Param("agent")    Agent agent
    );

    Optional<Client> findByUsername(String username);

}
