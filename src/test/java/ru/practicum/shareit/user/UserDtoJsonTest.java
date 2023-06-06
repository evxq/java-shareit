package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/*@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @SneakyThrows
    @Test
    void testUserDto() {
//        UserDto userDto = UserDto.builder().name("name").email("e@mail.com").build();
        UserDto userDto = new UserDto(1, "name", "e@mail.com");

        JsonContent<UserDto> result = json.write(userDto);

        result.extractingJsonPathNumberValue
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);

    }
}*/
