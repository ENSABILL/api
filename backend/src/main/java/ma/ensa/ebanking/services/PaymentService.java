package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.TransferDto;
import ma.ensa.ebanking.enums.AccountLimit;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.PaymentAccount;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

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

        // TODO: checking the credit card info and the balance (sending a HTTP request)

        // TODO END

        paymentRepository.feedAccount(
                account.getId(),
                dto.getAmount()
        );

    }


}