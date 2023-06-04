package ma.ensa.ebanking.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ma.ensa.ebanking.enums.AccountLimit;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientRequest extends AgentRequest{

    private String cin;

    private String dob;

    @JsonProperty("accountLimit")
    private AccountLimit desiredAccountLimit;

}