package ma.ensa.ebanking.dto.auth;

import lombok.Data;

@Data
public class AgentRequest {

    private String fullName;

    private String username;

    private String email;

    private String phoneNumber;
}
