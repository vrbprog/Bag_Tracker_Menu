package model.dao;

import model.User;

import java.io.IOException;
import java.util.List;

public interface UserDao {

    void saveUser(User user) throws IOException;

    void deleteUser(User user) throws IOException;

    List<User> getAll();

    User getUserByName(String userName);

    void updateUser(User user, String[] params) throws IOException;
}
