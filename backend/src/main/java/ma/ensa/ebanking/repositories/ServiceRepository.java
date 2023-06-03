package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.dto.ServiceDTO;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT new ma.ensa.ebanking.dto.ServiceDTO(s.name, s.type) FROM Service s " +
            "WHERE (:keyword is null or s.id = :keyword) " +
            "OR (:keyword is null or s.name LIKE %:keyword%) " +
            "AND (:type is null or s.type = :type)")
    List<ServiceDTO> findServiceByIdOrNameAndType(@Param("keyword") String keyword, @Param("type") ServiceType type);
}