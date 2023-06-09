package ma.ensa.ebanking.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.mapper.OperationMapper;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.*;
import ma.ensa.ebanking.request.AddFactureRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Transactional
@AllArgsConstructor
public class OperationService {
    private ServiceRepository serviceRepository;

    private ServiceProductRepository serviceProductRepository;

    private UserRepository userRepository;

    private OperationRepository operationRepository;

    private AgencyRepository agencyRepository;

    private PaymentService paymentService;

    public List<OperationDto> getAllClientsBills() throws Exception {
        if (!AuthService.Auths.checkAgent()) throw new PermissionException();

        Agent agent = AuthService.Auths.getAgent();

        return operationRepository.findAll().stream().filter(
                        operation -> operation.getService().getAgency().getImm().equals(agent.getAgency().getImm())
                )
                .map(OperationMapper::toDto).collect(Collectors.toList());
    }


    public OperationDto addBill(AddFactureRequest addFactureRequest) {
        if (!(AuthService.Auths.checkAgent())) throw new PermissionException();
        Client client = (Client) userRepository.findByUsername(addFactureRequest.getClientUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not Found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(addFactureRequest.getServiceId()).orElseThrow();
        Operation operation = Operation.builder()
                .client(client)
                .service(service)
                .amount(addFactureRequest.getAmount())
                .operationStatus(OperationStatus.UNPAID)
                .build();
        return OperationMapper.toDto(operationRepository.save(operation));
    }


    public List<OperationDto> getClientUnpaidBills(String serviceId) {

        Client client = AuthService.Auths.getClient();

        Stream<Operation> unpaidBills = client
                .getOperations()
                .stream()
                .filter(
                        operation -> operation.getOperationStatus().equals(OperationStatus.UNPAID)
                );
        if (serviceId != null) {
            unpaidBills = unpaidBills.filter(operation -> operation.getService().getId().equals(serviceId));
        }
        return unpaidBills.map(OperationMapper::toDto).collect(Collectors.toList());
    }

    public List<OperationDto> getClientPaidBills() {
        Client client = AuthService.Auths.getClient();
        return client
                .getOperations()
                .stream()
                .filter(
                        operation -> operation.getOperationStatus() == OperationStatus.PAID
                )
                .map(
                        operation -> {
                            if (operation.getService() != null) {
                                operation.setService(new ma.ensa.ebanking.models.Service());
                            }
                            return operation;
                        }
                )
                .map(OperationMapper::toDto)
                .toList();
    }
}
