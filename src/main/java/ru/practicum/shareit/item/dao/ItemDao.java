package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    Item addItem(Item item);

    Item updateItem(Integer userId, Item item);

    Item getItemById(Integer itemId);

    List<Item> getItemsForUser(Integer userId);

    List<Item> searchItemByText(String text);

}
