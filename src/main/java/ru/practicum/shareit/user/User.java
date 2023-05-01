package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {

    private Integer id;
    private String name;
    @NotNull(message = "e-mail должен быть заполнен")
    @Email(message = "e-mail некорректен")
    private String email;

}
