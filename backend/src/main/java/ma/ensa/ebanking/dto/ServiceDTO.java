package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ma.ensa.ebanking.enums.ServiceType;

@Data
public class ServiceDTO {

    private String name;

    private ServiceType type;

    ServiceDTO(){}

    ServiceDTO(String name, ServiceType type){
        this.name = name;
        this.type = type;
    }

}
