package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user, Integer userId);

    User getUserById(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer userId);

}
