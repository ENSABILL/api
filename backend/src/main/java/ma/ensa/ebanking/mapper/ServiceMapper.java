package ma.ensa.ebanking.mapper;

import ma.ensa.ebanking.dto.AgencyWithoutServicesDto;
import ma.ensa.ebanking.dto.ServiceDto;
import ma.ensa.ebanking.models.Service;
import ma.ensa.ebanking.models.ServiceProduct;

public class ServiceMapper {
    static public ServiceDto toDto(Service service) {
        AgencyWithoutServicesDto agencyDto = AgencyWithoutServicesDto.builder()
                .image(service.getAgency().getImage())
                .immId(service.getAgency().getImm())
                .patentId(service.getAgency().getPatentId())
                .name(service.getAgency().getName())
                .build();
        return ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .agency(agencyDto)
                .type(service.getType())
                .products(service.getProducts().stream().map(ServiceProduct::getName).toList())
                .build();
    }
}
