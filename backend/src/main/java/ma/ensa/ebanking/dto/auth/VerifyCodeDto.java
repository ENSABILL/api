package ma.ensa.ebanking.dto.auth;

import lombok.Data;

@Data
public class VerifyCodeDto {

    private String token;

    private String code;
}
