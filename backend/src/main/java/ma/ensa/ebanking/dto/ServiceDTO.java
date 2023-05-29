package ma.ensa.ebanking.dto;

import lombok.Data;
import ma.ensa.ebanking.enums.ServiceType;

@Data
public class ServiceDTO {

    private String name;

    private ServiceType type;

}
