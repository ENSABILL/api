package ma.ensa.ebanking.services;

import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;
import ma.ensa.ebanking.config.TwilioConfig;
import ma.ensa.ebanking.dto.LoginTokenDto;
import ma.ensa.ebanking.dto.PasswordResetResponseDto;
import ma.ensa.ebanking.enums.OtpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioOTPService {
    @Autowired
    private TwilioConfig twilioConfig;

    Map<String, String> otpMap = new HashMap<>();

    public Mono<PasswordResetResponseDto> sendOTPForPasswordReset(LoginTokenDto passwordResetRequestDto) {

        PasswordResetResponseDto passwordResetResponseDto = null;
        try {
            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMessage = "Dear Customer , Your OTP is ##" + otp + "##. Use this Passcode to complete your transaction. Thank You.";
            Message message = Message
                    .creator(to, from, otpMessage)
                    .create();
            otpMap.put(passwordResetRequestDto.getUserName(), otp);
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception ex) {
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.FAILED, ex.getMessage());
        }
        return Mono.just(passwordResetResponseDto);
    }

    @Deprecated(forRemoval = true)
    public Mono<String> validateOTP(String userInputOtp, String userName) {
        if (userInputOtp.equals(otpMap.get(userName))) {
            otpMap.remove(userName,userInputOtp);
            return Mono.just("Valid OTP please proceed with your transaction !");
        } else {
            return Mono.error(new IllegalArgumentException("Invalid otp please retry !"));
        }
    }

    //6 digit otp
    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}
