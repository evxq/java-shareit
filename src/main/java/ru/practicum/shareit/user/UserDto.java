package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    private Integer userId;
    private String name;
    @NotNull(message = "e-mail должен быть заполнен")
    @Email(message = "e-mail некорректен")
    private String email;

}
