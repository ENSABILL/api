package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.enums.OrderStatus;
import ma.ensa.ebanking.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<ProductOrder, String> {

    List<ProductOrder> findAllByProduct_Id(String productId);

    @Modifying
    @Query("UPDATE ProductOrder SET orderStatus = :status WHERE id = :id")
    void updateState(
            @Param("id") String orderId,
            @Param("status") OrderStatus status);
}
