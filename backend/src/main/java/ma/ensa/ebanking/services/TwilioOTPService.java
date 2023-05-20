package ma.ensa.ebanking.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.config.TwilioConfig;
import ma.ensa.ebanking.dto.auth.LoginTokenDTO;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioOTPService {

    private final TwilioConfig config;

    public void sendOTP(LoginTokenDTO dto){

        PhoneNumber from = new PhoneNumber(config.getTrialPhoneNumber()),
                    to = new PhoneNumber(dto.getPhoneNumber());

        String body = String.format("""
                hello motherfucker, the OTP is : %s
                """,
        dto.getOtp());

        Message.creator(
                to,
                from,
                body
        ).create();

    }

}
