package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product, String> {


    List<Product> findAllByAgency_Imm(String imm);

    @Modifying
    @Query("UPDATE Product SET qte = qte - :qte WHERE id = :productId")
    void decrementQte(
            @Param("productId") String productId,
            @Param("qte") int orderQte
    );
}
