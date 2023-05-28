package ma.ensa.ebanking.dto;

import lombok.Data;
import ma.ensa.ebanking.models.CreditCard;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@Data
public class TransferDto  {

    private String creditCardNumber;

    private int cvv;

    private String exp;

    private double amount;


}