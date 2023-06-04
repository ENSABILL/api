package ma.ensa.ebanking.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.dto.ServiceDto;
import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.mapper.OperationMapper;
import ma.ensa.ebanking.mapper.ServiceMapper;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.ServiceProduct;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.*;
import ma.ensa.ebanking.request.AddDonationRequest;
import ma.ensa.ebanking.request.AddFactureRequest;
import ma.ensa.ebanking.request.AddRechargeRequest;
import ma.ensa.ebanking.request.PayBillRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ServicesService {

    private ServiceRepository serviceRepository;

    private ServiceProductRepository serviceProductRepository;

    private UserRepository userRepository;

    private OperationRepository operationRepository;

    private AgencyRepository agencyRepository;

    public OperationDto addDonation(AddDonationRequest donationRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ServiceProduct product = serviceProductRepository.findById(donationRequest.getServiceId()).orElseThrow(() -> new RuntimeException("Service not found"));

        //TODO Payment

        Operation operation = Operation.builder()
                .serviceProduct(product)
                .client(client)
                .amount(donationRequest.getAmount())
                .operationStatus(OperationStatus.PAID)
                .operationTime(LocalDateTime.now())
                .build();
        client.getOperations().add(operation);
        product.getOperations().add(operation);
        return OperationMapper.mapOperation(operationRepository.save(operation));
    }

    public OperationDto addRecharge(AddRechargeRequest rechargeRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ServiceProduct product = serviceProductRepository.findById(rechargeRequest.getServiceId()).orElseThrow(() -> new RuntimeException("Service not found"));

        //TODO Payment

        Operation operation = Operation.builder()
                .serviceProduct(product)
                .client(client)
                .amount(rechargeRequest.getAmount().getAmount())
                .operationStatus(OperationStatus.PAID)
                .operationTime(LocalDateTime.now())
                .build();
        return OperationMapper.mapOperation(operationRepository.save(operation));
    }

    public OperationDto addBill(AddFactureRequest addFactureRequest) {
        if(!(AuthService.Auths.checkAgent())) throw new PermissionException();
        Client client = (Client) userRepository.findByUsername(addFactureRequest.getClientUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not Found"));
        ServiceProduct service = serviceProductRepository.findById(addFactureRequest.getServiceId()).orElseThrow();
        Operation operation = Operation.builder()
                .client(client)
                .serviceProduct(service)
                .amount(addFactureRequest.getAmount())
                .operationStatus(OperationStatus.UNPAID)
                .build();
        return OperationMapper.mapOperation(operationRepository.save(operation));
    }

    public OperationDto payBill(PayBillRequest payBillRequest) {
        Operation operation = operationRepository.findById(payBillRequest.getOperationId()).orElseThrow();

        //TODO Payment

        operation.setOperationStatus(OperationStatus.PAID);
        operation.setOperationTime(LocalDateTime.now());

        return OperationMapper.mapOperation(operationRepository.save(operation));
    }

    public List<OperationDto> getClientUnpaidBills() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return client.getOperations().stream().filter(operation -> operation.getOperationStatus().equals(OperationStatus.UNPAID)).map(OperationMapper::mapOperation).collect(Collectors.toList());
    }

    public List<OperationDto> getClientPaidBills() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return client.getOperations().stream().filter(operation -> operation.getOperationStatus().equals(OperationStatus.PAID)).map(OperationMapper::mapOperation).collect(Collectors.toList());
    }


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

        List<ServiceProduct> serviceProducts = dto.getProducts().stream().map(serviceProductName -> {
            return ServiceProduct.builder()
                    .name(serviceProductName)
                    .service(service)
                    .build();
        }).toList();

        service.setProducts(serviceProducts);
        // return the new id
        return serviceRepository
                .save(service)
                .getId();
    }

    public void toggleService(String id) throws Exception {

        if(!AuthService.Auths.checkAdmin()) throw new PermissionException();

        if (!serviceRepository.toggleService(id)) {
            throw new RecordNotFoundException("service not found");
        }

    }
}
