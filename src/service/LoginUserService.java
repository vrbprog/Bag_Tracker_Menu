package service;

import model.dao.UserDao;
import model.User;

public class LoginUserService implements UserService{

    private final UserDao<User> userDao;

    public LoginUserService(UserDao<User> userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean loginIsBusy(String username) {
        return userDao.getAll().stream().filter(user ->
                user.getLogin().equals(username)).count() == 1;
    }

    @Override
    public boolean login(String username, String password) {
        return userDao.getAll().stream().filter(user ->
                user.getLogin().equals(username) &&
                user.getPassword().equals(password)).count() == 1;
    }


}
