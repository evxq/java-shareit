package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.Valid;
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
                           @Valid @RequestBody ItemDto itemDto) {
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
    public List<ItemDtoBooking> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        if (from < 0 || size < 1) {
            throw new ValidationException("Некорректные параметры пагинации");
        }
        return itemService.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        if (from < 0 || size < 1) {
            throw new ValidationException("Некорректные параметры пагинации");
        }
        return itemService.searchItemByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }

}
