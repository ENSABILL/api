package ma.ensa.ebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.AccountLimit;
import ma.ensa.ebanking.models.user.Client;

@Data @NoArgsConstructor @AllArgsConstructor @Builder

@Entity
public class PaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountLimit accountLimit;

    @OneToOne
    @JsonIgnore
    private Client client;

    @PrePersist
    public void init(){
        this.balance = .0;
        this.accountLimit = AccountLimit.ACC_200;
    }
}
