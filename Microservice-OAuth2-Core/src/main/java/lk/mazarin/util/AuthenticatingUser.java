package lk.mazarin.util;

/**
 * Created by Kasun Perera on 6/12/2016.
 */
public class AuthenticatingUser {
    public final String USERNAME;
    public final String PASSWORD;
    public final String ROLE;

    public AuthenticatingUser(String username, String password, String role){
        this.USERNAME = username;
        this.PASSWORD = password;
        this.ROLE = role;

    }
}
