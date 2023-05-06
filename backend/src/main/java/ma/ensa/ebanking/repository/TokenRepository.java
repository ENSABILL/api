package ma.ensa.ebanking.repository;

import ma.ensa.ebanking.models.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository
        extends JpaRepository<LoginToken, String> {
}
