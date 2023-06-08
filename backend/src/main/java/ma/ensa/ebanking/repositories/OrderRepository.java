package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.enums.OrderStatus;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.ProductOrder;
import ma.ensa.ebanking.models.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderRepository
        extends JpaRepository<ProductOrder, String> {

    List<ProductOrder> findAllByProduct_Agency(Agency agency);

    List<ProductOrder> findAllByClient(Client client);

    @Modifying
    @Query("UPDATE ProductOrder SET orderStatus = :status WHERE id = :id")
    void updateState(
            @Param("id") String orderId,
            @Param("status") OrderStatus status);
}
