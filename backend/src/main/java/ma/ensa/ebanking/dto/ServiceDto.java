package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.ServiceType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private String id;

    private String name;

    private ServiceType type;

    private AgencyWithoutServicesDto agency;

    private List<String> products;

}
