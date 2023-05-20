package ma.ensa.ebanking.resource;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
@Deprecated
public class TwilioRouterConfig {

    private TwilioOTPHandler handler;

    @Bean
    public RouterFunction<ServerResponse> handleSMS() {
        return RouterFunctions.route()
                .POST("/router/sendOTP", handler::sendOTP)
                //.POST("/router/validateOTP", handler::validateOTP)
                .build();
    }
}
