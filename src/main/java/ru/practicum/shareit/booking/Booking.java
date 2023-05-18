package ru.practicum.shareit.booking;

import lombok.*;
import org.aspectj.lang.annotation.After;
import org.hibernate.Hibernate;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Нужно указать дату начала бронирования")
    @Future(message = "Начало бронирования не может быть в прошлом")
    @Column(name = "start_date")
    private LocalDateTime start;

    @NotNull(message = "Нужно указать дату конца бронирования")
    @Future(message = "Завершение бронирования не может быть в прошлом")
    @Column(name = "end_date")
    private LocalDateTime end;

    @NotNull(message = "Нужно указать объект бронирования")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;

    @NotNull(message = "Нужно указать пользователя-инициатора бронирования")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    @ToString.Exclude
    private User booker;

    private BookingStatus status;

    public Booking(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
