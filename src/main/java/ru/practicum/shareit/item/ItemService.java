package ru.practicum.shareit.item;

import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Integer userId, ItemDto itemDto);

    Item updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    Item getItemById(Integer itemId);

    List<Item> getItemsForUser(Integer userId);

    List<Item> searchItemByText(String text);

}
