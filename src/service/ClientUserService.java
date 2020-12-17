package service;

import model.User;
import model.dao.UserDao;

import java.io.IOException;

public class ClientUserService implements UserService{

    private final UserDao userDao;

    public ClientUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean loginIsBusy(String username) {
        return userDao.getAll().stream().filter(user ->
                user.getUserName().equals(username)).count() == 1;
    }

    @Override
    public boolean userRegistration(User user) {
        if(!loginIsBusy(user.getUserName())) {
            try {
                userDao.saveUser(user);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("********************");
                System.out.println("Error access to file DB");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean login(String username, String password) {
        return userDao.getAll().stream().filter(user ->
                user.getUserName().equals(username) &&
                user.getPassword().equals(password)).count() == 1;
    }

}
