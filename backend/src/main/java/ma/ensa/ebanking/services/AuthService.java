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
import ma.ensa.ebanking.services.utils.JwtService;
import ma.ensa.ebanking.services.utils.TwilioOTPService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final JwtService jwtService;
    public final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TwilioOTPService twilioOTPService;


    public String sendVerificationCode() {

        User user = Auths.getUser();

        Random random = new Random();
        String code = String.valueOf(random.nextInt(9000) + 1000);

        // TODO: send code via SMS or via Email
        LoginTokenDTO dto = LoginTokenDTO.builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .otp(code)
                .build();
        twilioOTPService.sendOTP(dto);

        // TODO: endTodo

        final long CURRENT_MILLIS = System.currentTimeMillis(),
                DURATION = 1000 * 60 * 60L; // 1 hour
        final Date
                expirationdDate = new Date(CURRENT_MILLIS + DURATION);

        OtpToken token = OtpToken.builder()
                .verificationCode(code)
                .user(user)
                .expireAt(expirationdDate)
                .build();

        token = tokenRepository.save(token);

        return token.getToken();
    }

    public void verifyCode(String tokenId, String code) throws Exception {

        OtpToken otpToken = tokenRepository.findById(tokenId)
                .orElseThrow(
                        () -> new RecordNotFoundException("token not found")
                );

        if (otpToken.expired()) {
            tokenRepository.deleteById(tokenId);
            throw new Exception("the token is expired");
        }

        if (!otpToken.getVerificationCode().equals(code)) {
            throw new Exception("the given code is wrong");
        }

        otpToken.setVerified(true);
        tokenRepository.save(otpToken);
    }

    public OtpToken verifyOtpToken(String token) {
        OtpToken otpToken = tokenRepository.findById(token)
                .orElseThrow(
                        () -> new RecordNotFoundException("token not found")
                );

        if (!otpToken.isVerified()) {
            throw new RuntimeException("you are forbidden to do this operation. get an otp code.");
        }

        tokenRepository.deleteById(token);

        if (otpToken.expired()) {
            throw new RuntimeException("the token is expired");
        }
        return otpToken;
    }

    public void resetPassword(ResetPasswordDto dto) {
        OtpToken otpToken = verifyOtpToken(dto.getToken());

        // reset the password
        userRepository.resetPassword(
                otpToken.getUser().getId(),
                passwordEncoder.encode(dto.getNewPassword())
        );

    }

    public AuthResponse authenticate(AuthRequest request) {

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

            if (user == null)
                throw new UnauthenticatedException();

            return user;
        }

        // Singleton
        public static boolean checkAdmin() {

            return getUser() instanceof Admin;
        }

        public static boolean checkAgent() {
            return getUser() instanceof Agent;
        }

        public static boolean checkAgentBelongsToAgency(String agencyImm) {
            boolean isAgent = checkAgent();
            if (!isAgent) return false;
            Agent agent = (Agent) getUser();
            return agent.getAgency().getImm().equals(agencyImm);
        }

        public static Agent getAgent() {
            try {
                return (Agent) getUser();
            } catch (ClassCastException e) {
                throw new PermissionException();
            }
        }

        public static Admin getAdmin() throws Exception {
            try {
                return (Admin) getUser();
            } catch (ClassCastException e) {
                throw new PermissionException();
            }
        }

        // TODO: for payment, transfer, ... etc
        public static Client getClient() throws RuntimeException {
            try {
                return (Client) getUser();
            } catch (ClassCastException e) {
                throw new PermissionException();
            }
        }

    }
}