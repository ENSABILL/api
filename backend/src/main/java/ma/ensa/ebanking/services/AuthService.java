package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.AuthRequest;
import ma.ensa.ebanking.dto.auth.AuthResponse;
import ma.ensa.ebanking.dto.auth.LoginTokenDTO;
import ma.ensa.ebanking.dto.auth.ResetPasswordDto;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.exceptions.UnauthenticatedException;
import ma.ensa.ebanking.models.user.*;
import ma.ensa.ebanking.repositories.TokenRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private final TwilioOTPService twilioOTPService;



    public String sendVerificationCode() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new RecordNotFoundException("user not found");
        }

        // deprecated
        String code = String.valueOf((int)(Math.random() * 100000));

        // TODO: send code via SMS or via Email
        LoginTokenDTO dto = LoginTokenDTO.builder()
                .username(username)
                .phoneNumber(user.get().getPhoneNumber())
                .otp(code)
                .build();
        twilioOTPService.sendOTP(dto);

        // TODO: endTodo

        final long CURRENT_MILLIS = System.currentTimeMillis(),
                DURATION = 1000 * 60 * 60L; // 1 hour
        final Date
                expirationdDate = new Date(CURRENT_MILLIS + DURATION);

        LoginToken token = LoginToken.builder()
                .verificationCode(code)
                .user(user.get())
                .expireAt(expirationdDate)
                .build();

        token = tokenRepository.save(token);

        return token.getToken();
    }

    public void verifyCode(String tokenId, String code) throws Exception {

        LoginToken loginToken =tokenRepository.findById(tokenId)
                .orElseThrow(
                        () -> new RecordNotFoundException("token not found")
                );

        if(loginToken.expired()) {
            tokenRepository.deleteById(tokenId);
            throw new Exception("the token is expired");
        }

        if(!loginToken.getVerificationCode().equals(code)){
            throw new Exception("the given code is wrong");
        }

        loginToken.setVerified(true);
        tokenRepository.save(loginToken);
    }

    public void resetPassword(ResetPasswordDto dto) throws Exception{

        LoginToken loginToken = tokenRepository.findById(dto.getToken())
                .orElseThrow(
                        () -> new RecordNotFoundException("token not found")
                );

        if(!loginToken.isVerified()){
            throw new Exception("you are forbidden to reset your password. get an otp before resetting password.");
        }

        tokenRepository.deleteById(dto.getToken());

        if(loginToken.expired()){
            throw new Exception("the token is expired");
        }

        // reset the password
        userRepository.resetPassword(
                loginToken.getUser().getId(),
                passwordEncoder.encode(dto.getNewPassword())
        );

    }

    public AuthResponse authenticate(AuthRequest request){

        User user = (User) userService.loadUserByUsername(
                request.getUsername()
        );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );

        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtService.generateToken(user))
                .userType(user.getClass().getSimpleName())
                .firstLogin(user.isFirstLogin())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public static class Auths {

        public static User getUser() throws UnauthenticatedException {

            User user = (User) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            if(user == null)
                throw new UnauthenticatedException();

            return user;
        }

        // Singleton
        public static void checkAdmin() throws Exception{
            if(getUser() instanceof Admin) return;
            throw new PermissionException();
        }

        public static Agent getAgent() throws Exception{
            try {
                return (Agent) getUser();
            }catch (ClassCastException e){
                throw new PermissionException();
            }
        }

        // TODO: for payment, transfer, ... etc
        public static Client getClient() throws Exception{
            try{
                return (Client) getUser();
            }catch (ClassCastException e){
                throw new PermissionException();
            }
        }

    }
}