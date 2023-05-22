package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public User createUser(User user) {
        return userDao.createUser(user);
    }

    public User updateUser(User user, Integer userId) {
        user.setId(userId);
        return userDao.updateUser(user);
    }

    public User getUserDyId(Integer userId) {
        return userDao.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void deleteUser(Integer userId) {
        userDao.deleteUser(userId);
    }

}
