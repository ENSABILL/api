package ma.ensa.ebanking.exceptions;

public class ServiceNotCompatibleException extends RuntimeException {
    public ServiceNotCompatibleException() {
    }

    public ServiceNotCompatibleException(String message) {
        super(message);
    }

    public ServiceNotCompatibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotCompatibleException(Throwable cause) {
        super(cause);
    }

    public ServiceNotCompatibleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
