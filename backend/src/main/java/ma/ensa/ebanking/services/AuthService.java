package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;

import ma.ensa.ebanking.config.JwtService;
import ma.ensa.ebanking.dto.LoginTokenDto;
import ma.ensa.ebanking.dto.auth.*;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.user.LoginToken;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.TokenRepository;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.logging.Handler;


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



    public String sendVerificationCode(String username) throws Exception {

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
        Optional<LoginToken> tokenOptional =
                tokenRepository.findById(tokenId);

        if(tokenOptional.isEmpty()){
            throw new RecordNotFoundException("token not found");
        }

        LoginToken token = tokenOptional.get();

        if(token.expired()) {
            tokenRepository.deleteById(tokenId);
            throw new Exception("the token is expired");
        }

        if(!token.getVerificationCode().equals(code)){
            throw new Exception("the given code is wrong");
        }


        token.setVerified(true);
        tokenRepository.save(token);
    }

    public void resetPassword(ResetPasswordDto dto) throws Exception{

        Optional<LoginToken> loginToken = tokenRepository.findById(dto.getToken());

        if(loginToken.isEmpty()){
            throw new Exception();
            //TODO: create an exception class and add it to AppExceptionHandler
        }

        LoginToken lt = loginToken.get();

        if(!lt.isVerified()){
            throw new Exception();
            //TODO: create an exception class and add it to AppExceptionHandler
        }

        tokenRepository.deleteById(dto.getToken());

        if(lt.expired()){
            throw new Exception();
            //TODO: create an exception class and add it to AppExceptionHandler
        }

        final String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        // TODO : reset the password
        userRepository.resetPassword(lt.getUser().getPhoneNumber(), encodedPassword);

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
                .token(jwtService.generateToken(user))
                .userType(user.getClass().getSimpleName())
                .build();
    }

}