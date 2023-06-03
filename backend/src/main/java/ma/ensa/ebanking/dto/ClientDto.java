package ma.ensa.ebanking.dto;

import lombok.Data;
import ma.ensa.ebanking.models.PaymentAccount;

@Data
public class ClientDto {

    private String fullName;

    private String username;

    private String phoneNumber;

    private String cin;

    private String email;

    private PaymentAccount account;
}
