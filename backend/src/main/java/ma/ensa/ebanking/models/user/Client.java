package ma.ensa.ebanking.models.user;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.PaymentAccount;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "client")
    List<Operation> operations = new ArrayList<>();

}
