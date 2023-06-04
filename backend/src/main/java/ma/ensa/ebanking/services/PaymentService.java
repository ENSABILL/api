package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.dto.TransferInternalDto;
import ma.ensa.ebanking.dto.TransferDto;
import ma.ensa.ebanking.enums.AccountLimit;
import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.mapper.OperationMapper;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.CreditCard;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.PaymentAccount;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.*;
import ma.ensa.ebanking.request.AddDonationRequest;
import ma.ensa.ebanking.request.AddRechargeRequest;
import ma.ensa.ebanking.request.PayBillRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CreditCardRepository creditCardRepository;

    private final ClientRepository clientRepository;
    private ServiceRepository serviceRepository;

    private UserRepository userRepository;

    private OperationRepository operationRepository;

    private boolean checkExp(String eDto, LocalDate eEnt){

        String[] arr = eDto.split("/");

        return (
                Integer.parseInt(arr[0]) == eEnt.getMonthValue()
                && Integer.parseInt(arr[1]) + 2000 == eEnt.getYear()
        );
    }

    public void createAccount(Client client, AccountLimit accountLimit){

        CreditCard creditCard = CreditCard.builder()
                .balance(1_000) // drna m3ak bat mzn
                .build();

        creditCardRepository.save(creditCard);

        PaymentAccount account = PaymentAccount
                .builder()
                .client(client)
                .accountLimit(accountLimit)
                .creditCard(creditCard)
                .build();

        paymentRepository.save(account);
    }

    public void upgradeAccount(int level) throws Exception {

        // check auth
        Client client = AuthService.Auths.getClient();

        // get the account
        if(client.getAccount() == null){
            throw new RecordNotFoundException("account not found");
        }
        PaymentAccount account = client.getAccount();


        AccountLimit newLimit = account
                .getAccountLimit()
                .upgrade(level);

        paymentRepository.upgradeAccount(account.getId(), newLimit);
    }

    public void feed(TransferDto dto) throws Exception{

        PaymentAccount account = AuthService.Auths
                .getClient()
                .getAccount();

        CreditCard creditCard =  creditCardRepository
                .findById(dto.getCreditCardNumber())
                .orElseThrow(
                        () -> new RecordNotFoundException("credit card not found")
                );

        if(
                creditCard.getCvv() != dto.getCvv()
                && !checkExp(dto.getExp(), creditCard.getExpirationDate())
        )
            throw new RecordNotFoundException("credit card not found");



        if(creditCard.getBalance() < dto.getAmount()){
            throw new Exception("insufficient amount");
        }

        creditCardRepository.descAmount(
                dto.getCreditCardNumber(),
                dto.getAmount()
        );

        paymentRepository.feedAccount(
                account.getId(), dto.getAmount()
        );


    }

    public void transferFromToAccount(TransferInternalDto dto) throws Exception{

        Client client = AuthService.Auths.getClient();

        PaymentAccount account = client.getAccount();

        if(account.getBalance() < dto.getAmount()){
            throw new Exception("insufficient balance");
        }

        PaymentAccount distAccount = clientRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(
                        () -> new RecordNotFoundException("client not found")
                ).getAccount();

        paymentRepository.feedAccount(
            account.getId(), -dto.getAmount()
        );

        paymentRepository.feedAccount(
                distAccount.getId(), dto.getAmount()
        );
    }

    public void transfer(Agency agency, double amount) throws Exception{

        PaymentAccount account = AuthService.Auths
                .getClient()
                .getAccount();

        if(account.getBalance() < amount){
            throw new RuntimeException("insufficient balance");
        }

        if(account.getAccountLimit().getLimit() < amount){
            throw new RuntimeException("""
                you cannot pass the limit,
                please upgrade your account
                """
            );
        }

        creditCardRepository.incrAmount(
                agency.getCreditCard().getCreditCardNumber()
                ,amount
        );

        paymentRepository.feedAccount(
                account.getId(), -amount
        );
    }

    public OperationDto payDonation(AddDonationRequest donationRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(donationRequest.getServiceId()).orElseThrow(() -> new RuntimeException("Service not found"));


        Agency agency = service.getAgency();

        try {
            transfer(agency, donationRequest.getAmount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Operation operation = Operation.builder()
                .service(service)
                .client(client)
                .amount(donationRequest.getAmount())
                .operationStatus(OperationStatus.PAID)
                .operationTime(LocalDateTime.now())
                .build();
        client.getOperations().add(operation);
        service.getOperations().add(operation);
        return OperationMapper.toDto(operationRepository.save(operation));
    }

    public OperationDto payRecharge(AddRechargeRequest rechargeRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(rechargeRequest.getServiceId()).orElseThrow(() -> new RuntimeException("Service not found"));


        Agency agency = service.getAgency();

        try {
            transfer(agency, rechargeRequest.getAmount().getAmount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Operation operation = Operation.builder()
                .service(service)
                .client(client)
                .amount(rechargeRequest.getAmount().getAmount())
                .operationStatus(OperationStatus.PAID)
                .operationTime(LocalDateTime.now())
                .build();
        return OperationMapper.toDto(operationRepository.save(operation));
    }

    public OperationDto payBill(PayBillRequest payBillRequest) {
        Operation operation = operationRepository.findById(payBillRequest.getOperationId()).orElseThrow();
        try {
            transfer(operation.getService().getAgency(), operation.getAmount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        operation.setOperationStatus(OperationStatus.PAID);
        operation.setOperationTime(LocalDateTime.now());

        return OperationMapper.toDto(operationRepository.save(operation));
    }

}