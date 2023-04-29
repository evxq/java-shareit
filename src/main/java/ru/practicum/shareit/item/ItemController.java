package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

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
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Integer itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getItemsForUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getItemsForUser(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItemByText(@RequestParam String text) {
        return itemService.searchItemByText(text);
    }

}
