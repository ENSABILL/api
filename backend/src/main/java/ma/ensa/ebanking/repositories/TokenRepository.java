package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.user.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository
        extends JpaRepository<LoginToken, String> {
}
