package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserAlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class UserDaoImpl implements UserDao {

    private int countId;
    private final List<User> userStorage = new ArrayList<>();

    @Override
    public User createUser(User user) {
        checkDuplicateEmail(user);
        user.setId(++countId);
        userStorage.add(user);
        log.info("Создан пользователь id={}", user.getId());

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getEmail() != null) {
            checkDuplicateEmail(user);
        }
        User updUser = null;
        for (User existedUser: userStorage) {
            if (user.getId().equals(existedUser.getId())) {
                if (user.getName() != null) {
                    existedUser.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    existedUser.setEmail(user.getEmail());
                }
                updUser = existedUser;
                break;
            } else {
                log.warn("Пользователь не найден");
                throw new NotFoundException("Такой пользователь не найден");
            }
        }
        log.info("Обновлен пользователь id={}", user.getId());

        return updUser;
    }

    @Override
    public User getUserDyId(Integer userId) {
        User user = null;
        for (User existedUser: userStorage) {
            if (userId.equals(existedUser.getId())) {
                user = existedUser;
            }
        }
        if (user == null) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Такой пользователь не найден");
        }
        log.info("Вызван пользователь id ={}", userId);

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Вызван список всех пользователей");

        return userStorage;
    }

    @Override
    public void deleteUser(Integer userId) {
        User delUser = null;
        for (User existedUser: userStorage) {
            if (userId.equals(existedUser.getId())) {
                delUser = existedUser;
            }
        }
        if (delUser == null) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Такой пользователь не найден");
        }
        userStorage.remove(delUser);
        log.info("Удален пользователь id={}", userId);
    }

    /*private int getId() {
        int lastId = userStorage.stream().mapToInt(User::getId).max().orElse(0);
        return lastId + 1;
    }*/

    private void checkDuplicateEmail(User user) {
        for (User existedUser: userStorage) {
            if (user.getEmail().equals(existedUser.getEmail()) && user.getId().equals(existedUser.getId())) {
                break;
            }
            if (user.getEmail().equals(existedUser.getEmail())) {
                log.warn("e-mail уже используется");
                throw new UserAlreadyExistException("Такой e-mail уже используется");
            }
        }
    }

}
