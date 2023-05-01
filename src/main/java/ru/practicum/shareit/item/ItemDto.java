package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    private Integer itemDtoId;
    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;
    @NotNull(message = "Не указано описание вещи")
    private String description;
    @NotNull(message = "Не указана доступность вещи")
    private Boolean available;

}
