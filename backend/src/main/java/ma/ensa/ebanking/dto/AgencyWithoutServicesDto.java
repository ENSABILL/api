package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyWithoutServicesDto {
    private String name;
    private String immId;
    private String patentId;
    private String image;
}
