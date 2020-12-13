package model.dao;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    boolean saveUser(User user);

    void deleteUser(User user);

    List<User> getAll();

    User getUserByName(String userName);

    void updateUser(User user, String[] params);
}
