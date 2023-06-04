package ma.ensa.ebanking.repositories;

import jakarta.transaction.Transactional;
import ma.ensa.ebanking.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface CreditCardRepository
        extends JpaRepository<CreditCard, String> {

    @Modifying
    @Query( "UPDATE CreditCard c SET c.amount = c.amount - :amount " +
            "WHERE c.creditCardNumber = :num")
    void descAmount(
            @Param("num") String num ,
            @Param("amount") double amount
    );

    @Modifying
    @Query( "UPDATE CreditCard c SET c.amount = c.amount + :amount " +
            "WHERE c.creditCardNumber = :num")
    void incrAmount(
            @Param("num") String num ,
            @Param("amount") double amount
    );
}