package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ServiceRepository
        extends JpaRepository<Service, String> {

    @Modifying
    @Query("UPDATE Service s SET s.active = false WHERE s.id = :id")
    boolean disableService(String id);

    @Modifying
    @Query("UPDATE Service s SET s.active = true WHERE s.id = :id")
    boolean enableService(String id);

}