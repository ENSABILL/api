package ma.ensa.ebanking.services;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.config.TwilioConfig;
import ma.ensa.ebanking.dto.auth.LoginTokenDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwilioOTPService {

    private final TwilioConfig config;

    public void sendOTP(LoginTokenDTO dto){

        String phoneNumber = dto.getPhoneNumber();

        if(phoneNumber.startsWith("06")){
            phoneNumber = "212" + phoneNumber.substring(3);
        }

        PhoneNumber from = new PhoneNumber(config.getTrialPhoneNumber()),
                    to = new PhoneNumber(phoneNumber);

        String body = String.format(" dear %s, the OTP is : %s ",
            dto.getUsername(),
            dto.getOtp()
        );

        Message.creator(to, from, body)
                .create();

    }

}
