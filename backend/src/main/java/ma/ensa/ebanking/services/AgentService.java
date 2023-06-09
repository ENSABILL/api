package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgentDto;
import ma.ensa.ebanking.dto.InitialPasswordDto;
import ma.ensa.ebanking.dto.auth.AgentRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.mapper.AgentMapper;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.AgencyRepository;
import ma.ensa.ebanking.repositories.AgentRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import ma.ensa.ebanking.services.utils.TwilioOTPService;
import ma.ensa.ebanking.utils.PasswordUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwilioOTPService twilioOTPService;
    private final AgentRepository agentRepository;

    public void createAgent(AgentRequest request) throws Exception {

        // check the auth
        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();

        // check the availability of agency
        Agency agency = agencyRepository.findById(request.getAgencyImmId()).orElseThrow(
                () -> {
                    throw new RecordNotFoundException("agency not found");
                }
        );

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
                 //.fullName(request.getFirstName() + " " + request.getLastName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .enabled(true)
                .firstLogin(true)
                .agency(agency)
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

    public List<AgentDto> getAllAgents(){

        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();

        return agentRepository.findAll().stream().map(AgentMapper::toDto).collect(Collectors.toList());
    }

    public void deleteAgent(String id){
        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();
        agentRepository.findById(id).orElseThrow(()->new RecordNotFoundException("Agent Not Found."));
        agentRepository.deleteById(id);
    }

}
