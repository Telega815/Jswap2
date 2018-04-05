package ru.jswap.dao.intefaces;

import ru.jswap.entities.User;

import java.util.List;

public interface UserDAO {

    List<User> getUsers();
    User getUser(Integer id);
    User getUser(String username);
    void saveUser(User user);
    void deleteUser(User user);
    void updateUser(User user);
}
