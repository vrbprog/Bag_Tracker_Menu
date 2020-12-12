package model.dao;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDaoInMemImpl implements UserDao{
    private final List<User> users = new ArrayList<>();

    public UserDaoInMemImpl() {
        users.add(new User("admin", "***"));
        users.add(new User("guest", "123"));
        users.add(new User("userTest", "123"));
    }

    @Override
    public User getUserByName(String userName) {
        Optional<User> optionalUser = users.stream()
                .filter(user -> user.getUserName()
                        .equals(userName)).findFirst();
        return optionalUser.orElseGet(User::new);
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
    public void updateUser(User user, String[]params) {
        int userIndex = users.indexOf(user);
        user.setUserName(Objects.requireNonNull(
                params[0], "Name cannot be null"));
        user.setPassword(Objects.requireNonNull(
                params[1], "Password cannot be null"));

        users.set(userIndex,user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
}