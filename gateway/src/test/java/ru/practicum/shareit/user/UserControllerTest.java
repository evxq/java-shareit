package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(1, "name", "e@mail.com");
    }

    @SneakyThrows
    @Test
    void createUser_returnUser() {
        UserDto userDto = UserMapper.toUserDto(user);
        ResponseEntity<Object> entity = new ResponseEntity<>(userDto, HttpStatus.OK);
        when(userClient.createUser(userDto)).thenReturn(entity);

        String result = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(userDto.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void createUser_userNotValid() {
        user.setEmail("mail");
        UserDto userDto = UserMapper.toUserDto(user);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).createUser(userDto);
    }

    @SneakyThrows
    @Test
    void updateUser_returnUser() {
        User updUser = new User(1, "name2", "e@mail2.com");
        UserDto userDto = UserMapper.toUserDto(updUser);
        ResponseEntity<Object> entity = new ResponseEntity<>(userDto, HttpStatus.OK);
        when(userClient.updateUser(any(), anyInt())).thenReturn(entity);

        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", updUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updUser)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(userDto.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(userDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(userDto.getEmail()), String.class))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient).updateUser(any(), eq(updUser.getId()));
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void getUserById_returnUser() {
        ResponseEntity<Object> entity = new ResponseEntity<>(user, HttpStatus.OK);
        when(userClient.getUserById(anyInt())).thenReturn(entity);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(user.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(user.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(user.getEmail()), String.class))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient).getUserById(user.getId());
        assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @SneakyThrows
    @Test
    void getAllUsers_returnList() {
        UserDto userDto = UserMapper.toUserDto(user);
        ResponseEntity<Object> entity = new ResponseEntity<>(List.of(userDto), HttpStatus.OK);
        when(userClient.getAllUsers()).thenReturn(entity);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(user.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is(user.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is(user.getEmail()), String.class));

        verify(userClient).getAllUsers();
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userClient).deleteUser(userId);
    }

}
