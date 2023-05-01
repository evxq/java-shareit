package ru.practicum.shareit.user;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user, Integer userId);

    User getUserDyId(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer userId);

}
