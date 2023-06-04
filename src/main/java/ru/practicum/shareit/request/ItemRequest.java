package ru.practicum.shareit.request;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.item.ItemDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "request")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Не указано описание запроса")
    @NotBlank(message = "Не указано описание запроса")
    private String description;

    @NotNull(message = "Не указано время создания запроса")
    private LocalDateTime created;

    @Column(name = "requester_id")
    @NotNull(message = "Не указан инициатор запроса")
    private Integer requesterId;

    private List<ItemDto> items = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemRequest that = (ItemRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
