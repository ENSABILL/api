package ma.ensa.ebanking.dto.auth;

import lombok.Data;

@Data
public class ActivateAccountDTO {

    private String phoneNumber;

    private String verificationCode;

}
