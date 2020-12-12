package service;

import model.dao.UserDao;

public class LoginUserService implements UserService{

    private final UserDao userDao;

    public LoginUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean loginIsBusy(String username) {
        return userDao.getAll().stream().filter(user ->
                user.getUserName().equals(username)).count() == 1;
    }

    @Override
    public boolean login(String username, String password) {
        return userDao.getAll().stream().filter(user ->
                user.getUserName().equals(username) &&
                user.getPassword().equals(password)).count() == 1;
    }


}
