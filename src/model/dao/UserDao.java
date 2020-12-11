package model.dao;

import java.util.List;
import java.util.Optional;

public interface UserDao<T> {

    void save(T t);

    void delete(T t);

    List<T> getAll();

    Optional<T> get(long id);

    void update(T t, String[]params);
}
