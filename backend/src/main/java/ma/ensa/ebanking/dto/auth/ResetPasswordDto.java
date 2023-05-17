package ma.ensa.ebanking.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordDto {

    private String token;

    private String newPassword;

}
