package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.ServiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProductRepository extends JpaRepository<ServiceProduct, String> {
}
