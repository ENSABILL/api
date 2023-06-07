package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
