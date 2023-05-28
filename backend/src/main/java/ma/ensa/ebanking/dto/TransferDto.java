package ma.ensa.ebanking.dto;

import lombok.Data;

@Data
public class TransferDto {

    private String creditCardNumber;

    private String cvv;

    private String exp;

    private double amount;
}