package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;

import ma.ensa.ebanking.config.JwtService;
import ma.ensa.ebanking.dto.auth.*;
import ma.ensa.ebanking.exceptions.EmailNotAvailableException;
import ma.ensa.ebanking.models.Agent;
import ma.ensa.ebanking.models.LoginToken;
import ma.ensa.ebanking.models.User;
import ma.ensa.ebanking.repository.TokenRepository;
import ma.ensa.ebanking.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtService jwtService;
    public final  AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;



    public void registerAgent(AgentRequest request) throws Exception{

        // check the auth
        Auths.getAdmin();

        // check the availability of username and email
        if(!(
            userRepository.existsByUsername(request.getUsername()) &&
            userRepository.existsByEmail(request.getEmail())
        ))
            throw new EmailNotAvailableException();


        // add agent
        User agent = Agent.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .fullName(request.getFirstName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.save(agent);

    }

    public void sendVerificationCode(String phoneNumber){
        // OPT stuff here
    }

    public void resetPassword(String token, String password) throws Exception{

        Optional<LoginToken> t = tokenRepository.findById(token);

        if(t.isEmpty()){
            throw new Exception();
            //TODO: create an exception class and add it to AppExceptionHandler
        }

        if(t.get().expired()){
            throw new Exception();
            //TODO: create an exception class and add it to AppExceptionHandler
        }


        final String encodedPassword =
                passwordEncoder.encode(password);


        tokenRepository.deleteById(token);


    }

    public AuthResponse authenticate(AuthRequest request) throws AuthenticationException {

        User user = (User) userService.loadUserByUsername(
                request.getUsername()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .userType(user.getClass().getSimpleName())
                .build();
    }

}