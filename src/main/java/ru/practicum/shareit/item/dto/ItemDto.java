package ru.practicum.shareit.item.dto;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Integer itemDtoId;
    @NotBlank(message = "Название вещи не может быть пустым")
    String name;
    @NotNull(message = "Не указано описание вещи")
    String description;
    @NotNull(message = "Не указана доступность вещи")
    Boolean available;

}
