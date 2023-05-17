package ma.ensa.ebanking.services;


import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.UnauthenticatedException;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.models.user.Admin;
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

    public static Agent getAgent() throws Exception{
        try {
            return (Agent) getUser();
        }catch (ClassCastException e){
            throw new PermissionException();
        }
    }

}
