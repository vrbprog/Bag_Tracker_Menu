package model.dao;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDaoInMemImpl implements UserDao {
    private final List<User> users = new ArrayList<>();

    public UserDaoInMemImpl() {
        users.add(new User("admin", "***"));
        users.add(new User("guest", "123"));
    }

    @Override
    public User getUserByName(String userName) {
        return users.stream()
                .filter(user -> user.getUserName()
                        .equals(userName)).findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void saveUser(User user) {
        users.add(user);
    }

    @Override
    public void updateUser(User oldUser, User newUser) {
        int userIndex = users.indexOf(oldUser);
        users.set(userIndex, newUser);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
}
