package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    Item addItem(Integer userId, ItemDto itemDto);

    Item updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    Item getItemById(Integer itemId);

    List<Item> getItemsForUser(Integer userId);

    List<Item> searchItemByText(String text);

}
