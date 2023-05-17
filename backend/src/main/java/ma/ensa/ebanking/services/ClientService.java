package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.ClientRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.repositories.ClientRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public void registerClient(ClientRequest request, boolean verify) throws Exception{

        Agent agent = (verify) ? Auths.getAgent() : null;

        // check the availability of username and email
        if(!(
            userRepository.existsByUsername(request.getUsername()) &&
            userRepository.existsByEmail(request.getEmail()) &&
            userRepository.existsByPhoneNumber(request.getPhoneNumber())
        ))
            throw new EmailNotAvailableException();

        User client = Client.builder()
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .CIN(request.getCIN())
            .dob(request.getDob())
            .fullName(request.getFullName())
            .username(request.getUsername())
            .verifiedBy(agent)
            .build();

        userRepository.save(client);
    }

    public void verifyAccount(String username) throws Exception{

        Agent agent = Auths.getAgent();

        Optional<Client> clientOptional = clientRepository.findByUsername(username);

        if(clientOptional.isEmpty()){
            throw new RecordNotFoundException("client not found");
        }

        clientRepository.setVerified(username, agent);

    }
}