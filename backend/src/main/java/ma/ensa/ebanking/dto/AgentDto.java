package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentDto {
    private String id;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String agencyImm;

}
