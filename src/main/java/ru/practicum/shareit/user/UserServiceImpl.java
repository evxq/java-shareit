package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //    private final UserDao userDao;
    private final UserRepository userRepository;

    public User createUser(User user) {
//        return userDao.createUser(user);
        /*if (!userRepository.checkDuplicateEmail(user.getEmail()).isEmpty()) {
            log.warn("e-mail уже используется");
            throw new UserAlreadyExistException("Такой e-mail уже используется");
        }*/
        /*if (user.getEmail() == null) {
            log.warn("Не указан e-mail");
            throw new ValidationException("Не указан e-mail");
        }*/
        User newUser = userRepository.save(user);
        log.info("Создан пользователь id={}", newUser.getId());

        return newUser;
    }

    public User updateUser(User user, Integer userId) {
        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден");
                    return new NotFoundException("Такой пользователь не найден");
                });
        if (user.getName() != null) {
            existedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existedUser.setEmail(user.getEmail());
        }
//        user.setId(userId);
        User updUser = userRepository.save(existedUser);
        log.info("Обновлен пользователь id={}", user.getId());
//        return userDao.updateUser(user);
        return updUser;
    }

    public User getUserById(Integer userId) {
        log.info("Вызван пользователь id={}", userId);

//        return userDao.getUserById(userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь не найден");
            return new NotFoundException("Такой пользователь не найден");
        });
    }

    public List<User> getAllUsers() {
        log.info("Вызван список всех пользователей");

//        return userDao.getAllUsers();
        return userRepository.findAll();
    }

    public void deleteUser(Integer userId) {
//        userDao.deleteUser(userId);
        userRepository.deleteById(userId);
        log.info("Удален пользователь id={}", userId);
    }

}
