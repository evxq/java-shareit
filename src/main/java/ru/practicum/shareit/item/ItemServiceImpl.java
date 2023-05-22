package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserDao;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public Item addItem(Integer userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (item.getName() == null || item.getName().isEmpty() || item.getDescription() == null || item.getAvailable() == null) {
            log.warn("Недостаточно данных для создания вещи");
            throw new ValidationException("Недостаточно данных для создания вещи");
        }
        item.setOwner(userDao.getUserById(userId));

        return itemDao.addItem(item);
    }

    @Override
    public Item updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        if (itemDto.getItemDtoId() == null) {
            itemDto.setItemDtoId(itemId);
        }
        return itemDao.updateItem(userId, ItemMapper.toItem(itemDto));
    }

    @Override
    public Item getItemById(Integer itemId) {
        return itemDao.getItemById(itemId);
    }

    @Override
    public List<Item> getItemsForUser(Integer userId) {
        return itemDao.getItemsForUser(userId);
    }

    @Override
    public List<Item> searchItemByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.warn("Вызван поиск вещей для пустой строки");
            return new ArrayList<>();
        }
        return itemDao.searchItemByText(text.toLowerCase());
    }

}
