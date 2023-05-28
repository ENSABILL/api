package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.AgentRequest;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final UserRepository userRepository;

    public void createAgent(AgentRequest request) throws Exception{

        // check the auth
        AuthService.Auths.checkAdmin();

        // check the availability of username and email
        if(
                userRepository.existsByUsername(request.getUsername()) ||
                userRepository.existsByEmail(request.getEmail())
        )
            throw new EmailNotAvailableException();


        // add agent
        User agent = Agent.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.save(agent);

    }

}
