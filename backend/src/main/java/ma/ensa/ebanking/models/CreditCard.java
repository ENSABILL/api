package ma.ensa.ebanking.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.ensa.ebanking.models.user.Client;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class CreditCard {

    @Id
    private String cardNumber; // contains 16 digits

    private int cvv;

    private LocalDate expiryDate;

    @ManyToOne
    private Client client;


}
