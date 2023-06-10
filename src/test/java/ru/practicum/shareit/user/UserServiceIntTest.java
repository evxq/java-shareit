package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntTest {

    private final EntityManager em;
    private final UserService userService;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        userDto = UserDto.builder().name("name").email("e@mail.com").build();
    }

    @Test
    void createUser_returnSavedUser() {
        userService.createUser(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUser_returnUpdatedUser() {
        UserDto createdUserDto = userService.createUser(userDto);
        UserDto updUserDto = UserDto.builder().name("newName").email("new@mail.com").build();

        userService.updateUser(updUserDto, createdUserDto.getId());
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        User user = query.setParameter("email", updUserDto.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(updUserDto.getName()));
        assertThat(user.getEmail(), equalTo(updUserDto.getEmail()));
    }

    @Test
    void getUserById_returnUser() {
        UserDto userDto = userService.createUser(this.userDto);

        User userById = userService.getUserById(userDto.getId());
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        User user = query.setParameter("id", userDto.getId()).getSingleResult();

        assertThat(userById.getId(), notNullValue());
        assertThat(userById.getName(), equalTo(user.getName()));
        assertThat(userById.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getAllUsers_returnListSize() {
        UserDto userDto2 = UserDto.builder().name("name2").email("e2@mail.com").build();
        UserDto userDto3 = UserDto.builder().name("name3").email("e3@mail.com").build();
        userService.createUser(userDto);
        userService.createUser(userDto2);
        userService.createUser(userDto3);

        List<UserDto> users = userService.getAllUsers();

        assertThat(users, hasSize(3));
        for (UserDto user : users) {
            assertThat(users, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail()))
            )));
        }
    }

    @Test
    void deleteUser() {
        UserDto userDto = userService.createUser(this.userDto);

        userService.deleteUser(userDto.getId());
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);

        assertThrows(NoResultException.class,
                () -> query.setParameter("id", userDto.getId()).getSingleResult());
    }

}
