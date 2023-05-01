package ru.practicum.shareit.user;

import java.util.List;

public interface UserDao {

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer userId);

}
