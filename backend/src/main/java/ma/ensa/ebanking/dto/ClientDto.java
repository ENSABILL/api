package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.models.PaymentAccount;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private String id;

    private String fullName;

    private String username;

    private String phoneNumber;

    private String cin;

    private String email;

    private PaymentAccount account;
}
