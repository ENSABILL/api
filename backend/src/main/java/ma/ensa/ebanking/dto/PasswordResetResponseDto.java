package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.OtpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponseDto {
    private OtpStatus status;
    private String message;
}
