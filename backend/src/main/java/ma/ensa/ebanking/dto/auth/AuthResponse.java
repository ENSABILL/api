package ma.ensa.ebanking.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private String userType;
    private boolean firstLogin;
    private String id;
    private String email;
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String fullName;
}
