package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.dto.PayRechargesRequest;
import ma.ensa.ebanking.dto.TransferDto;
import ma.ensa.ebanking.dto.TransferInternalDto;
import ma.ensa.ebanking.enums.AccountLimit;
import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.enums.RechargeAmount;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.exceptions.*;
import ma.ensa.ebanking.mapper.OperationMapper;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.CreditCard;
import ma.ensa.ebanking.models.Operation;
import ma.ensa.ebanking.models.PaymentAccount;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.*;
import ma.ensa.ebanking.request.PayBillsRequest;
import ma.ensa.ebanking.request.PayDonationRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CreditCardRepository creditCardRepository;

    private final ClientRepository clientRepository;
    private final ServiceRepository serviceRepository;

    private final UserRepository userRepository;

    private final OperationRepository operationRepository;
    private final AuthService authService;

    private boolean checkExp(String eDto, LocalDate eEnt) {

        String[] arr = eDto.split("/");

        return (
                Integer.parseInt(arr[0]) == eEnt.getMonthValue()
                        && Integer.parseInt(arr[1]) + 2000 == eEnt.getYear()
        );
    }

    public void createAccount(Client client, AccountLimit accountLimit) {

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
        if (client.getAccount() == null) {
            throw new RecordNotFoundException("account not found");
        }
        PaymentAccount account = client.getAccount();


        AccountLimit newLimit = account
                .getAccountLimit()
                .upgrade(level);

        paymentRepository.upgradeAccount(account.getId(), newLimit);
    }

    public void feed(TransferDto dto) throws Exception {

        PaymentAccount account = AuthService.Auths
                .getClient()
                .getAccount();

        CreditCard creditCard = creditCardRepository
                .findById(dto.getCreditCardNumber())
                .orElseThrow(
                        () -> new RecordNotFoundException("credit card not found")
                );

        if (
                creditCard.getCvv() != dto.getCvv()
                        && !checkExp(dto.getExp(), creditCard.getExpirationDate())
        )
            throw new RecordNotFoundException("credit card not found");


        if (creditCard.getBalance() < dto.getAmount()) {
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

    public void transferFromToAccount(TransferInternalDto dto) throws Exception {

        Client client = AuthService.Auths.getClient();

        PaymentAccount account = client.getAccount();

        if (account.getBalance() < dto.getAmount()) {
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


    public void checkBalanceAndLimit(double amount) {

        PaymentAccount account = AuthService.Auths
                .getClient()
                .getAccount();

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("insufficient balance");
        }

        if (account.getAccountLimit().getLimit() < amount) {
            throw new AccountLimitException("""
                    you cannot pass the limit,
                    please upgrade your account
                    """
            );
        }

    }

    public void transfer(Agency agency, double amount) {

        PaymentAccount account = AuthService.Auths
                .getClient()
                .getAccount();

        checkBalanceAndLimit(amount);

        creditCardRepository.incrAmount(
                agency.getCreditCard().getCreditCardNumber()
                , amount
        );

        paymentRepository.feedAccount(
                account.getId(), -amount
        );
    }

    public OperationDto payDonation(PayDonationRequest donationRequest) {
        authService.verifyOtpToken(donationRequest.getToken());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(donationRequest.getServiceId()).orElseThrow(() -> new RuntimeException("Service not found"));
        if (!service.getType().equals(ServiceType.DONATION))
            throw new ServiceNotCompatibleException("the service is not a donation service");

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
                .build();
        //client.getOperations().add(operation);
        service.getOperations().add(operation);
        return OperationMapper.toDto(operationRepository.save(operation));
    }


    private OperationDto payRecharge(String serviceId, float amount, String token) {
        authService.verifyOtpToken(token);

        if (!RechargeAmount.checkAmountIsValid(amount)) {
            throw new RechargeAmountNotSupportedException("Recharge amount : " + amount + " not supported");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ma.ensa.ebanking.models.Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));
        if (!service.getType().equals(ServiceType.RECHARGE))
            throw new ServiceNotCompatibleException("the service is not a recharge service");
        Client client = (Client) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));


        Agency agency = service.getAgency();

        try {
            transfer(agency, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Operation operation = Operation.builder()
                .service(service)
                .client(client)
                .amount(amount)
                .operationStatus(OperationStatus.PAID)
                .operationTime(LocalDateTime.now())
                .build();
        return OperationMapper.toDto(operationRepository.save(operation));
    }

    public List<OperationDto> payRecharges(PayRechargesRequest payRechargesRequest) {
        authService.verifyOtpToken(payRechargesRequest.getToken());
        return payRechargesRequest.getAmounts().stream().map(amount -> payRecharge(payRechargesRequest.getServiceId(), amount, payRechargesRequest.getToken())).filter(Objects::nonNull).toList();
    }

    private OperationDto payBill(Long operationId) {
        Operation operation = operationRepository.findByIdAndOperationStatus(operationId, OperationStatus.UNPAID).orElse(null);
        if (operation == null) return null;
        try {
            transfer(operation.getService().getAgency(), operation.getAmount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        operation.setOperationStatus(OperationStatus.PAID);
        operation.setOperationTime(LocalDateTime.now());

        return OperationMapper.toDto(operationRepository.save(operation));
    }

    public List<OperationDto> payBills(PayBillsRequest payBillsRequest) {
        authService.verifyOtpToken(payBillsRequest.getToken());
        return payBillsRequest.getOperationsIds().stream().map(this::payBill).filter(Objects::nonNull).toList();
    }

}