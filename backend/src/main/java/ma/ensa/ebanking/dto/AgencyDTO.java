package ma.ensa.ebanking.dto;

import lombok.Builder;
import lombok.Data;
import ma.ensa.ebanking.models.Service;

import java.util.List;

@Data
@Builder
public class AgencyDTO {
    private String name;
    private String immId;
    private String patentId;
    private String image;

    private List<ServiceDto> services;
}
