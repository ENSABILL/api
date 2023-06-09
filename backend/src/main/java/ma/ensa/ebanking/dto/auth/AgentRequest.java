package ma.ensa.ebanking.dto.auth;

import lombok.Data;

@Data
public class AgentRequest {

    private String firstName;
    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private String agencyImmId;
}
