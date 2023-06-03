package ma.ensa.ebanking.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientRequest extends AgentRequest{

    private String cin;

    private String dob;


}