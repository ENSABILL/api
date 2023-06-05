package ma.ensa.ebanking.repositories;

import ma.ensa.ebanking.models.user.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository
        extends JpaRepository<OtpToken, String> {
}
