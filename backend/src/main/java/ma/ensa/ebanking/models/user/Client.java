package ma.ensa.ebanking.models.user;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.ensa.ebanking.models.PaymentAccount;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Client extends User {

    private String CIN;

    private String dob;

    @ManyToOne
    private Agent verifiedBy;

    @OneToOne(mappedBy = "client")
    private PaymentAccount account;

}
