package ma.ensa.ebanking.controllers;

import lombok.Data;
import ma.ensa.ebanking.models.PaymentAccount;

@Data
public class ClientDto {

    private String fullName;

    private String username;

    private String cin;

    private String email;

    private PaymentAccount account;
}
