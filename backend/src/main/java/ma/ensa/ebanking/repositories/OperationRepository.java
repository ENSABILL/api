package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Long> {
    Optional<Operation> findByIdAndOperationStatus(Long id, OperationStatus operationStatus);
}
