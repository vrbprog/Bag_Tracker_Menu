package model.dao;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MemoryUserDao implements UserDao<User>{
    private final List<User> users = new ArrayList<>();

    public MemoryUserDao() {
        users.add(new User("admin", "***"));
        users.add(new User("guest", "123"));
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get((int) id));
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public void update(User user, String[]params) {
        user.setLogin(Objects.requireNonNull(
                params[0], "Name cannot be null"));
        user.setPassword(Objects.requireNonNull(
                params[1], "Email cannot be null"));

        users.add(user);
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }
}
