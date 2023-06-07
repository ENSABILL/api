package ma.ensa.ebanking.models.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.ensa.ebanking.enums.AccountLimit;
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

    @Column(nullable = false)
    private String cin;

    private String dob;

    @ManyToOne
    private User verifiedBy;

    @OneToOne(mappedBy = "client")
    private PaymentAccount account;

    @OneToMany(mappedBy = "client")
    List<Operation> operations;

    @Enumerated(EnumType.STRING)
    private AccountLimit desiredAccountLimit;

}
