package ma.ensa.ebanking.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.ServiceDto;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.mapper.ServiceMapper;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.ServiceProduct;

import ma.ensa.ebanking.repositories.*;

import ma.ensa.ebanking.repositories.AgencyRepository;
import ma.ensa.ebanking.repositories.OperationRepository;
import ma.ensa.ebanking.repositories.ServiceRepository;
import ma.ensa.ebanking.repositories.UserRepository;


import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@Transactional
@AllArgsConstructor
public class ServicesService {

    private ServiceRepository serviceRepository;

    private ServiceProductRepository serviceProductRepository;

    private UserRepository userRepository;

    private OperationRepository operationRepository;

    private AgencyRepository agencyRepository;



    public List<ServiceDto> getAllServices(String searchQuery, ServiceType type) {
        List<ma.ensa.ebanking.models.Service> serviceList = new ArrayList<>();
        if (type != null) {
            if (searchQuery != null) {

                serviceList = serviceRepository.findAllByTypeAndNameContainingIgnoreCase(type, searchQuery);

            } else {
                serviceList = serviceRepository.findAllByType(type);
            }
        } else {
            if (searchQuery != null) {
                serviceList = serviceRepository.findAllByNameContainingIgnoreCase(searchQuery);
            } else {
                serviceList = serviceRepository.findAll();
            }
        }
        return serviceList.stream().map(ServiceMapper::toDto).toList();
    }

    public ServiceDto getService(String serviceId) {
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(serviceId).orElseThrow(
                () -> new RecordNotFoundException("record not found")
        );
        return ServiceMapper.toDto(service);
    }

    public String addService(String imm, ServiceDto dto) throws Exception {

        // check the permission : agent or admin are the ones able to create service

        boolean checkAdmin = AuthService.Auths.checkAdmin();
        boolean checkAgent = (AuthService.Auths.checkAgent() && AuthService.Auths.checkAgentBelongsToAgency(imm));
        if(!checkAgent && !checkAdmin) throw new PermissionException();

        // get the agency
        Agency agency = agencyRepository.findById(imm)
                .orElseThrow(
                        () -> new RecordNotFoundException("record not found")
                );

        // add the service
        final ma.ensa.ebanking.models.Service service = ma.ensa.ebanking.models.Service.builder()
                .agency(agency)
                .name(dto.getName())
                .type(dto.getType())
                .build();

        List<ServiceProduct> serviceProducts = dto.getProducts()
                .stream()
                .map(name ->
                    ServiceProduct.builder()
                        .name(name)
                        .service(service)
                        .build()
                ).toList();

        service.setProducts(serviceProducts);
        // return the new id
        return serviceRepository
                .save(service)
                .getId();
    }

    public void toggleService(String id) {

        if(!AuthService.Auths.checkAdmin()) throw new PermissionException();

        if (!serviceRepository.toggleService(id)) {
            throw new RecordNotFoundException("service not found");
        }

    }
}
