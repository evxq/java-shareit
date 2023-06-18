package shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(classes = User.class)
/*@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @SneakyThrows
    @Test
    void testUserDto_returnUserDto() {
        UserDto userDto = UserDto.builder().name("name").email("e@mail.com").build();
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.email");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("e@mail.com");
    }

}*/
