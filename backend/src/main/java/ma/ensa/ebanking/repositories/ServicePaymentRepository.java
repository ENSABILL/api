package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.ServicePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePaymentRepository
        extends JpaRepository<ServicePayment, String> {
}
