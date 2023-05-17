package ma.ensa.ebanking.models;

import jakarta.persistence.ManyToOne;
import ma.ensa.ebanking.enums.AccountLimit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    @ManyToOne
    private Client client;
}
