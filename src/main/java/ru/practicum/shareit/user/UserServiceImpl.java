package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

//    private final UserDao userDao;
    private final UserRepository userRepository;

    public User createUser(User user) {
//        return userDao.createUser(user);
        log.info("Создан пользователь id={}", user.getId());

        return userRepository.save(user);
    }

    public User updateUser(User user, Integer userId) {
        user.setId(userId);
        log.info("Обновлен пользователь id={}", user.getId());

//        return userDao.updateUser(user);
        return userRepository.save(user);
    }

    public User getUserById(Integer userId) {
        log.info("Вызван пользователь id={}", userId);

//        return userDao.getUserById(userId);
//        return userRepository.getReferenceById(userId);                                   !!!!!!!!!!!!!!!!!!!!!!!!!1
        return userRepository.getById(userId);
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
