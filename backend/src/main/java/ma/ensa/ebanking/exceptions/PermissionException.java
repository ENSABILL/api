package ma.ensa.ebanking.exceptions;

public class PermissionException extends Exception{

    public PermissionException(){
        super("you are not permitted");
    }

    public PermissionException(String message){
        super(message);
    }

}
