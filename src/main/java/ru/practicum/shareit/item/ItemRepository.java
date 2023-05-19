package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOwnerId(Integer userId);

    @Query("SELECT it " +
            "FROM Item it" +
            "WHERE UPPER(it.name) LIKE UPPER(CONCAT('%', ?1, '%')) OR UPPER(it.description) LIKE UPPER(CONCAT('%', ?1, '%')) AND it.available = true")
    List<Item> findAllByTextContaining(String text);

}
