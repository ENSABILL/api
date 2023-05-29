package ma.ensa.ebanking.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class LoginToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String token;

    @ManyToOne
    private User user;

    private Date expireAt;

    private boolean verified;


    @Column(length = 8)
    private String verificationCode;



    public boolean expired(){
        return expireAt.before(new Date());
    }
}