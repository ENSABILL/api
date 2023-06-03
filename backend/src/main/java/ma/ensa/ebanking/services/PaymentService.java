package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.controllers.TransferInternalDto;
import ma.ensa.ebanking.dto.TransferDto;
import ma.ensa.ebanking.enums.AccountLimit;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.CreditCard;
import ma.ensa.ebanking.models.PaymentAccount;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.ClientRepository;
import ma.ensa.ebanking.repositories.CreditCardRepository;
import ma.ensa.ebanking.repositories.PaymentRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CreditCardRepository creditCardRepository;

    private final ClientRepository clientRepository;

    private boolean checkExp(String eDto, LocalDate eEnt){

        String[] arr = eDto.split("/");

        return (
                Integer.parseInt(arr[0]) == eEnt.getMonthValue()
                && Integer.parseInt(arr[1]) + 2000 == eEnt.getYear()
        );
    }

    public void createAccount(Client client){
        PaymentAccount account = PaymentAccount
                .builder()
                .client(client)
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

        Client client = AuthService.Auths.getClient();

        PaymentAccount account = client.getAccount();

        if(account.getBalance() + dto.getAmount() > account.getAccountLimit().getLimit()){
            throw new PermissionException("you cannot pass the limit");
        }

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



        if(creditCard.getAmount() < dto.getAmount()){
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

    public void transfer(TransferInternalDto dto) throws Exception{

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
}