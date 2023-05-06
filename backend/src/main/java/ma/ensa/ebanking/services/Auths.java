package ma.ensa.ebanking.services;


import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.UnauthenticatedException;
import ma.ensa.ebanking.models.User;
import ma.ensa.ebanking.models.Admin;
import org.springframework.security.core.context.SecurityContextHolder;


public class Auths {

    public static User getUser() throws UnauthenticatedException {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if(user == null)
            throw new UnauthenticatedException();

        return user;
    }



    public static Admin getAdmin() throws Exception{
        try {
            return (Admin) getUser();
        }catch (ClassCastException e){
            throw new PermissionException();
        }
    }

}
