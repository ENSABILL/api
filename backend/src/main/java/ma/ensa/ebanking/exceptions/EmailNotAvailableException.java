package ma.ensa.ebanking.exceptions;

public class EmailNotAvailableException extends Exception{

    public EmailNotAvailableException(){
        super("email or username not available");
    }

}
