package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.InitialPasswordDto;
import ma.ensa.ebanking.dto.auth.AgentRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.UserRepository;
import ma.ensa.ebanking.utils.PasswordUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwilioOTPService twilioOTPService;

    public void createAgent(AgentRequest request) throws Exception {

        // check the auth
        AuthService.Auths.checkAdmin();

        // check the availability of username and email
        if (
                userRepository.existsByUsername(request.getUsername()) ||
                        userRepository.existsByEmail(request.getEmail())
        )
            throw new EmailNotAvailableException();

        String generatedPassword = PasswordUtil.generateRandomPassword(6);
        // add agent
        User agent = Agent.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .fullName(request.getFirstName() + " " + request.getLastName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .enabled(true)
                .firstLogin(true)
                .password(passwordEncoder.encode(generatedPassword))
                .build();
        InitialPasswordDto dto = InitialPasswordDto.builder()
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .password(generatedPassword)
                .build();
        twilioOTPService.sendInitialPassword(dto);
        userRepository.save(agent);

    }

}
