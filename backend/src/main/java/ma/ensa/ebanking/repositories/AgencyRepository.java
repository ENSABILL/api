package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository
        extends JpaRepository<Agency, String> {}
