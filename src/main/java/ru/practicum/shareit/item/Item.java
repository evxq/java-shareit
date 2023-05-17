package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Getter @Setter @ToString
@RequiredArgsConstructor
@Builder
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;                                        // @Column не указан, т.к названия поля и колонки совпадают

    @NotNull(message = "Не указано описание вещи")
    private String description;                                 // @Column не указан ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User owner;

    @NotNull(message = "Не указана доступность вещи")
    private Boolean available;                                  // @Column не указан ...

    private Integer requestId;

}
