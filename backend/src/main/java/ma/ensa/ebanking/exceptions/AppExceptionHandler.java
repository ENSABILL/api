package ma.ensa.ebanking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArguments(
            MethodArgumentNotValidException ex
    ) {
        final Map<String, String> map = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                field -> map.put(field.getField(), field.getDefaultMessage())
        );
        return map;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ProviderNotAccepted.class)
    public String handleProviderNotAccepted(ProviderNotAccepted ex) {
        return ex.getMessage();
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthenticatedException.class)
    public String handleUnAuthentication() {
        return "you are not authenticated";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionException.class)
    public String handlePermission(PermissionException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecordNotFoundException.class)
    public String handleNotFoundRecord(RecordNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailNotAvailableException.class)
    public String handleEmailNotAvailable(EmailNotAvailableException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RechargeAmountNotSupportedException.class)
    public String handleRechargeAmountNotSupported(RechargeAmountNotSupportedException ex) {
        return ex.getMessage();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceNotCompatibleException.class)
    public String handleServiceNotCompatible(ServiceNotCompatibleException ex) {
        return ex.getMessage();
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(
            AuthenticationException ex
    ) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Exception.class)
    public String handleOtherException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InsufficientBalanceException.class)
    public String handleInsufficientBalanceException(InsufficientBalanceException ex){
        return ex.getMessage();
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccountLimitException.class)
    public String handleAccountLimitException(AccountLimitException ex){
        return ex.getMessage();
    }
}
