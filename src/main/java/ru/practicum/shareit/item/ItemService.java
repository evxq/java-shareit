package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    ItemDtoBooking getItemById(Integer itemId);

    List<ItemDtoBooking> getItemsForUser(Integer userId);

    List<ItemDto> searchItemByText(String text);

    CommentDto addComment(Integer userId, Integer itemId, CommentDto commentDto);

}
