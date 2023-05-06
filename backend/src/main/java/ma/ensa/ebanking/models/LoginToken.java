package ma.ensa.ebanking.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class LoginToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String token;

    @ManyToOne
    private User user;

    private LocalDateTime expireAt;

    @Column(length = 8)
    private String verificationCode;

    public boolean expired(){
        return expireAt.isAfter(LocalDateTime.now());
    }
}