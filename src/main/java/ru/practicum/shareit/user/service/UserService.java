package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user, Integer userId);

    User getUserDyId(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer userId);

}
