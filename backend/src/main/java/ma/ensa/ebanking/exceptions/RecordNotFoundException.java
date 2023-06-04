package ma.ensa.ebanking.exceptions;

public class RecordNotFoundException extends RuntimeException{

    public RecordNotFoundException(String message){
        super(message);
    }

}
