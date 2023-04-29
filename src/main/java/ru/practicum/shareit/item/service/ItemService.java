package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Integer userId, ItemDto itemDto);

    Item updateItem(Integer userId, ItemDto itemDto);

    Item getItemById(Integer itemId);

    List<Item> getItemsForUser(Integer userId);

    List<Item> searchItemByText(String text);

}
