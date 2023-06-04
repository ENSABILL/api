package ma.ensa.ebanking.exceptions;

public class EmailNotAvailableException extends RuntimeException{

    public EmailNotAvailableException(){
        super("email or username not available");
    }

}
