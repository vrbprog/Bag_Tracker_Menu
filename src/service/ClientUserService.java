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
    public boolean userRegistration(User user) throws IOException {
        if(!loginIsBusy(user.getUserName())) {
            userDao.saveUser(user);
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
