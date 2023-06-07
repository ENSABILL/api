package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findAllByAgency_Imm(String imm);
}
