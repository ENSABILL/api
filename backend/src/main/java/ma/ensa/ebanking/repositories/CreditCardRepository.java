package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository
        extends JpaRepository<CreditCard, String> {}
