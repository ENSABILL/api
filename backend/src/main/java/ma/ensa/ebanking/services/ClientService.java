package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ClientDto;
import ma.ensa.ebanking.dto.InitialPasswordDto;
import ma.ensa.ebanking.dto.auth.ClientRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.ClientRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import ma.ensa.ebanking.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ma.ensa.ebanking.services.AuthService.Auths;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PaymentService paymentService;
    private final PasswordEncoder passwordEncoder;
    private final TwilioOTPService twilioOTPService;

    public void createClient(ClientRequest request, boolean verify) throws Exception {

        Agent agent = (verify) ? Auths.getAgent() : null;

        // check the availability of username and email
        if (
                userRepository.existsByUsername(request.getUsername()) &&
                userRepository.existsByEmail(request.getEmail())
        )
            throw new EmailNotAvailableException();

        Client client = Client.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .CIN(request.getCin())
                .dob(request.getDob())
                .enabled(verify)
                .firstLogin(true)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .desiredAccountLimit(request.getDesiredAccountLimit())
                .username(request.getUsername())
                .verifiedBy(agent)
                .build();

        if (verify) {
            String generatedPassword = PasswordUtil.generateRandomPassword(6);
            client.setPassword(passwordEncoder.encode(generatedPassword));
            InitialPasswordDto dto = InitialPasswordDto.builder()
                    .username(request.getUsername())
                    .phoneNumber(request.getPhoneNumber())
                    .password(generatedPassword)
                    .build();
            System.out.println(generatedPassword);
            twilioOTPService.sendInitialPassword(dto);
        }

        clientRepository.save(client);

        if (verify) {
            paymentService.createAccount(client, request.getDesiredAccountLimit());
        }
    }

    public void verifyAccount(String username) throws Exception {

        // check permission
        Agent agent = Auths.getAgent();

        // get the client
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RecordNotFoundException("client not found")
                );
        String generatedPassword = PasswordUtil.generateRandomPassword(6);
        InitialPasswordDto dto = InitialPasswordDto.builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .password(generatedPassword)
                .build();
        // set verified
        clientRepository.setVerified(username, agent, passwordEncoder.encode(generatedPassword));
        // send generated password
        twilioOTPService.sendInitialPassword(dto);
        // create a payment account for the client
        paymentService.createAccount((Client) user, ((Client) user).getDesiredAccountLimit());
    }

    public ClientDto getClient(String username) throws Exception {

        Optional<Client> clientOpt =
                clientRepository.findByUsername(username);

        Client client = clientOpt.isPresent() ? clientOpt.get() : Auths.getClient();

        ClientDto clientDto = new ClientDto();
        BeanUtils.copyProperties(client, clientDto);
        return clientDto;
    }

}