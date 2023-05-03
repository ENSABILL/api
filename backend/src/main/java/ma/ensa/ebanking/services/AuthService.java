package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;

import ma.ensa.ebanking.config.JwtService;
import ma.ensa.ebanking.dto.auth.AuthRequest;
import ma.ensa.ebanking.dto.auth.AuthResponse;
import ma.ensa.ebanking.dto.auth.RegisterRequest;
import ma.ensa.ebanking.models.User;
import ma.ensa.ebanking.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    public final  AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;



    public AuthResponse register(RegisterRequest request) throws Exception{


        return AuthResponse.builder()
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) throws AuthenticationException {

        User user = (User) userService.loadUserByUsername(
                request.getEmail()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .userType(user.getClass().getSimpleName())
                .build();
    }

}