package ru.practicum.shareit.item;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;                                        // @Column не указан, т.к названия поля и колонки совпадают

    @NotNull(message = "Не указано описание вещи")
    private String description;                                 // @Column не указан, т.к названия поля и колонки совпадают

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    private User owner;

    @Column(name = "is_available")
    @NotNull(message = "Не указана доступность вещи")
    private Boolean isAvailable;

    private Integer requestId;

    public Item(Integer itemDtoId, String name, String description, Boolean available) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
