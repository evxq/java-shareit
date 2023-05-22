package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Integer id;

    @NotNull(message = "Нужно указать дату начала бронирования")
    @Future(message = "Начало бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Нужно указать дату конца бронирования")
    @Future(message = "Завершение бронирования не может быть в прошлом")
    private LocalDateTime end;

    @NotNull(message = "Нужно указать объект бронирования")
    private Integer itemId;
    private Integer bookerId;
    private String status;

}
