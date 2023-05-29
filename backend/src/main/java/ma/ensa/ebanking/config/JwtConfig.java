package ma.ensa.ebanking.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secretKey;
    private Long duration;

    public Long getDuration() {
        // convert duration from minutes to milliseconds
        return duration * 60_000;
    }

    public Date getExperationDate() {
        return new Date(
                System.currentTimeMillis()
                + getDuration()
        );
    }
}
