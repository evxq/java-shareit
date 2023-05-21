package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ItemService {

    ItemDto addItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    ItemDtoBooking getItemDtoBookingById(Integer itemId, Integer userId);

    Item getItemById(Integer itemId);

    List<ItemDtoBooking> getItemsForUser(Integer userId);

    List<ItemDto> searchItemByText(String text);

    CommentDto addComment(Integer userId, Integer itemId, CommentDto commentDto);

}
