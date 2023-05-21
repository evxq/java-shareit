package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @PathVariable Integer itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoBooking getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @PathVariable Integer itemId) {
        return itemService.getItemDtoBookingById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoBooking> getItemsForUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text) {
        return itemService.searchItemByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }

}
