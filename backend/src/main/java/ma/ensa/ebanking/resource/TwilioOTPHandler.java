package ma.ensa.ebanking.resource;

import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.LoginTokenDto;
import ma.ensa.ebanking.services.TwilioOTPService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Deprecated
public class TwilioOTPHandler {

    private TwilioOTPService service;

    public Mono<ServerResponse> sendOTP(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginTokenDto.class)
                .flatMap(dto -> service.sendOTPForPasswordReset(dto))
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .body(BodyInserters.fromValue(dto)));
    }

    /*
    public Mono<ServerResponse> validateOTP(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginTokenDto.class)
                .flatMap(dto -> service.validateOTP(dto.getOneTimePassword(), dto.getUserName()))
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(dto));
    }

     */

}
