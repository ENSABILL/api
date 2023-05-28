package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.enums.AccountLimit;
import ma.ensa.ebanking.models.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<PaymentAccount, String> {

    @Modifying
    @Query("UPDATE PaymentAccount SET accountLimit = :limit WHERE id = :id")
    void upgradeAccount(
            @Param("id")    String id,
            @Param("limit") AccountLimit newLimit
    );

    @Modifying
    @Query(value = "UPDATE PaymentAccount p SET p.balance = p.balance + :amount WHERE p.id = :id")
    void feedAccount(
            @Param("id") String id,
            @Param("amount") double amount
    );

}
