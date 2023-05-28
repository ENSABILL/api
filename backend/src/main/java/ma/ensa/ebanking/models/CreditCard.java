package ma.ensa.ebanking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class CreditCard {

    @Id
    private String creditCardNumber;

    private int cvv;

    private LocalDate expirationDate, creationDate;

    private double amount;

    @PrePersist
    public void init(){

        // init the cardNumber
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<16; i++)
            sb.append(
                random.nextInt(10)
            );
        creditCardNumber = sb.toString();

        // init the cvv
        random = new Random();
        cvv = random.nextInt(900) + 100;

        // init the expiration date
        creationDate = LocalDate.now();
        expirationDate = creationDate.plusYears(5);

        // init the amount
        amount = 10_000;
    }

}