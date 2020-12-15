package service;

import model.User;

import java.io.IOException;

public interface UserService {

    /**
     * Used to login a user
     * @param username user name
     * @param password user password
     * @return outcome of login - success or not
     */
    boolean login(String username, String password);

    boolean loginIsBusy(String username);

    boolean userRegistration(User user) throws IOException;


}
