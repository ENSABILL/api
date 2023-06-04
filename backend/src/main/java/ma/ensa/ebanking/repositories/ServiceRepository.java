package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ServiceRepository
        extends JpaRepository<Service, String> {

    @Modifying
    @Query(
            value = "UPDATE Service SET active = NOT(active) WHERE id = :id",
            nativeQuery = true
    )
    boolean toggleService(String id);

    List<Service> findAllByTypeAndNameContainingIgnoreCase(ServiceType type, String searchQuery);
    List<Service> findAllByType(ServiceType type);

    List<Service> findAllByNameContainingIgnoreCase(String searchQuery);
}