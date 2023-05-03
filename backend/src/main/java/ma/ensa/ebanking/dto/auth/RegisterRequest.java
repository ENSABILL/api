package ma.ensa.ebanking.dto.auth;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterRequest {

    // TODO: delete 'provider' flag
    private boolean provider;
    private String fullName;
    @Email(message = "invalid email address")
    private String email;

    private String phoneNumber;
    private String password;

    private String city;
    
}