package ma.ensa.ebanking.dto;

import lombok.Data;

@Data
public class TransferDto  {

    private String creditCardNumber;

    private int cvv;

    private String exp;

    private String username;

    private double amount;

}