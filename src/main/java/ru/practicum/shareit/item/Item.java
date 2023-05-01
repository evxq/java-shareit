package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {

    private Integer id;
    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;
    @NotNull(message = "Не указано описание вещи")
    private String description;
    private User owner;
    @NotNull(message = "Не указана доступность вещи")
    private Boolean available;
    private Integer requestId;

}
