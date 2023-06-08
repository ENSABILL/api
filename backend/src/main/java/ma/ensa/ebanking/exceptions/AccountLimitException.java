package ma.ensa.ebanking.exceptions;

public class AccountLimitException extends RuntimeException{
    public AccountLimitException() {
    }

    public AccountLimitException(String message) {
        super(message);
    }

    public AccountLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountLimitException(Throwable cause) {
        super(cause);
    }

    public AccountLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
