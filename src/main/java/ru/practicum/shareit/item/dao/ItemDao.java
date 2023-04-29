package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemDao {

    Item addItem(Item item);

    Item updateItem(Integer userId, Item item);

    Item getItemDyId(Integer itemId);

    List<Item> getItemsForUser();

    List<Item> searchItemByText(String text);

}
