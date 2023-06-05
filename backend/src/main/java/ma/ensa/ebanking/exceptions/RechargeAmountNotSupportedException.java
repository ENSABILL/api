package ma.ensa.ebanking.exceptions;

public class RechargeAmountNotSupportedException extends RuntimeException {
    public RechargeAmountNotSupportedException() {
    }

    public RechargeAmountNotSupportedException(String message) {
        super(message);
    }

    public RechargeAmountNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RechargeAmountNotSupportedException(Throwable cause) {
        super(cause);
    }

    public RechargeAmountNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
