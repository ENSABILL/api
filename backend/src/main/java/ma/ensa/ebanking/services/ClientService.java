package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.ClientRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.ClientRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import static ma.ensa.ebanking.services.AuthService.Auths;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PaymentService paymentService;

    public void createClient(ClientRequest request, boolean verify) throws Exception{

        Agent agent = (verify) ? Auths.getAgent() : null;

        // check the availability of username and email
        if(!(
            userRepository.existsByUsername(request.getUsername()) &&
            userRepository.existsByEmail(request.getEmail())
        ))
            throw new EmailNotAvailableException();

        Client client = Client.builder()
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .CIN(request.getCIN())
            .dob(request.getDob())
            .fullName(request.getFullName())
            .username(request.getUsername())
            .verifiedBy(agent)
            .build();

        userRepository.save(client);

        if(verify){
            paymentService.createAccount(client);
        }
    }

    public void verifyAccount(String username) throws Exception{

        // check permission
        Agent agent = Auths.getAgent();

        // get the client
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RecordNotFoundException("client not found")
                );
        // set verified
        clientRepository.setVerified(username, agent);
        // create a payment account for the client
        paymentService.createAccount((Client) user);
    }
}