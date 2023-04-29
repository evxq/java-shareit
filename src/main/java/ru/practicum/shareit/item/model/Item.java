package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    Integer id;
    @NotBlank(message = "Название вещи не может быть пустым")
    String name;
    @NotNull(message = "Не указано описание вещи")
    String description;
    User owner;
    @NotNull(message = "Не указана доступность вещи")
    Boolean available;

}
