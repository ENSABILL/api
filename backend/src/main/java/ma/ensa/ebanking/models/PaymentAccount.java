package ma.ensa.ebanking.models;

import jakarta.persistence.*;
import ma.ensa.ebanking.enums.AccountLimit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.models.user.Client;

@Data @NoArgsConstructor @AllArgsConstructor @Builder

@Entity
public class PaymentAccount {

    @Id
    @GeneratedValue
    private long id;

    private double balance;

    private AccountLimit accountLimit;

    @OneToOne(mappedBy = "client")
    private Client client;
}
