package service;

import model.User;
import model.dao.UserDao;

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
        return userDao.saveUser(user);
    }

    @Override
    public boolean login(String username, String password) {
        return userDao.getAll().stream().filter(user ->
                user.getUserName().equals(username) &&
                user.getPassword().equals(password)).count() == 1;
    }

}
