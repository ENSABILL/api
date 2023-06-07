package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ClientDto;
import ma.ensa.ebanking.dto.InitialPasswordDto;
import ma.ensa.ebanking.dto.auth.ClientRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.mapper.ClientMapper;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.ClientRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import ma.ensa.ebanking.request.UpdateClientRequest;
import ma.ensa.ebanking.request.UpdatePasswordRequest;
import ma.ensa.ebanking.services.utils.TwilioOTPService;
import ma.ensa.ebanking.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        User verifiedBy = null;
        if (verify) {
            if (Auths.checkAdmin()) verifiedBy = Auths.getAdmin();
            if (Auths.checkAgent()) verifiedBy = Auths.getAgent();
        }

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
                .verifiedBy(verifiedBy)
                .build();

        if (verify) {
            String generatedPassword = PasswordUtil.generateRandomPassword(6);
            client.setPassword(passwordEncoder.encode(generatedPassword));
            InitialPasswordDto dto = InitialPasswordDto.builder()
                    .username(request.getUsername())
                    .phoneNumber(request.getPhoneNumber())
                    .password(generatedPassword)
                    .build();
            twilioOTPService.sendInitialPassword(dto);
        }

        clientRepository.save(client);

        if (verify) {
            paymentService.createAccount(client, request.getDesiredAccountLimit());
        }
    }

    public void verifyAccount(String username) throws Exception {
        boolean checkAdmin = Auths.checkAdmin();
        boolean checkAgent = Auths.checkAgent();
        if (!checkAdmin && !checkAgent) throw new PermissionException();

        User verifiedBy = null;
        // check permission
        if (checkAdmin) verifiedBy = Auths.getAdmin();
        if (checkAgent) verifiedBy = Auths.getAgent();

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
        clientRepository.setVerified(username, verifiedBy, passwordEncoder.encode(generatedPassword));
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

    public double checkBalance() throws Exception {
        Client client = AuthService.Auths.getClient();
        return client.getAccount().getBalance();
    }

    public List<ClientDto> getAllClients(){
        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();
        return clientRepository.findAll().stream().map(ClientMapper::toDto).collect(Collectors.toList());
    }

    public void deleteClient(String id){
        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();
        clientRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Client not found"));
        clientRepository.deleteById(id);
    }

    public ClientDto updateClient(UpdateClientRequest updateClientRequest) throws Exception {
        Client client = AuthService.Auths.getClient();

        if(!clientRepository.existsByEmail(updateClientRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        if(!clientRepository.existsByUsername(updateClientRequest.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        client.setEmail(Objects.requireNonNullElse(updateClientRequest.getEmail(),client.getEmail()));
        client.setUsername(Objects.requireNonNullElse(updateClientRequest.getUsername(),client.getUsername()));
        return ClientMapper.toDto(clientRepository.save(client));
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) throws Exception {
        Client client = AuthService.Auths.getClient();
        boolean oldPasswordMatches = passwordEncoder.matches(updatePasswordRequest.getOldPassword(), client.getPassword());
        if (!oldPasswordMatches) {
            throw new RuntimeException("The old password is incorrect");
        }
        String encodedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        client.setPassword(encodedPassword);

        clientRepository.save(client);
    }

}