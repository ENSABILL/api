package ma.ensa.ebanking.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.enums.FactureStatus;
import ma.ensa.ebanking.mapper.OperationMapper;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.OperationRepository;
import ma.ensa.ebanking.repositories.ServiceRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import ma.ensa.ebanking.request.AddDonationRequest;
import ma.ensa.ebanking.request.AddFactureRequest;
import ma.ensa.ebanking.request.AddRechargeRequest;
import ma.ensa.ebanking.request.PayBillRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ServicesService {

    private ServiceRepository serviceRepository;

    private UserRepository userRepository;

    private OperationRepository operationRepository;

    private PaymentService paymentService;

    OperationDto addDonation(AddDonationRequest donationRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(donationRequest.getServiceId()).orElseThrow(()->new RuntimeException("Service not found"));

        //TODO Verify Balance

        Operation operation = Operation.builder()
                .service(service)
                .client(client)
                .amount(donationRequest.getAmount())
                .factureStatus(FactureStatus.PAID)
                .build();
        client.getOperations().add(operation);
        service.getOperations().add(operation);
        return OperationMapper.mapOperation(operationRepository.save(operation));
    }

    OperationDto addRecharge(AddRechargeRequest rechargeRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(rechargeRequest.getServiceId()).orElseThrow(()->new RuntimeException("Service not found"));

        //TODO Verify Balance

        Operation operation = Operation.builder()
                .service(service)
                .client(client)
                .amount(rechargeRequest.getAmount().getAmount())
                .factureStatus(FactureStatus.PAID)
                .build();
        return OperationMapper.mapOperation(operationRepository.save(operation));
    }

    //adds a bill to the client
    OperationDto addBill(AddFactureRequest addFactureRequest){
        Client client =(Client) userRepository.findByUsername(addFactureRequest.getClientUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not Found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(addFactureRequest.getServiceId()).orElseThrow();
        Operation operation = Operation.builder()
                .client(client)
                .service(service)
                .amount(addFactureRequest.getAmount())
                .factureStatus(FactureStatus.UNPAID)
                .build();
    return OperationMapper.mapOperation(operationRepository.save(operation));
    }
    OperationDto payBill(PayBillRequest payBillRequest){
        return null;
    }

    List<OperationDto> getClientUnpaidBills(){
        return null;
    }

    List<OperationDto> getClientPaidBills(){
        return null;
    }

}
