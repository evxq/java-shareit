package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Repository
public class ItemDaoImpl implements ItemDao {

    private int countId;
    private final List<Item> itemStorage = new ArrayList<>();

    @Override
    public Item addItem(Item item) {
        item.setId(++countId);
        itemStorage.add(item);
        log.info("Создана вещь id={}", item.getId());

        return item;
    }

    @Override
    public Item updateItem(Integer userId, Item item) {
        Item updItem = null;
        for (Item existedItem : itemStorage) {
            if (item.getId().equals(existedItem.getId())) {
                if (userId != item.getOwner().getId()) {
                    log.warn("Некорректный пользователь");
                    throw new NotFoundException("Некорректный пользователь");
                }
                if (item.getName() != null) {
                    existedItem.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    existedItem.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    existedItem.setAvailable(item.getAvailable());
                }
                updItem = existedItem;
                break;
            } else {
                log.warn("Вещь не найдена");
                throw new NotFoundException("Такая вещь не найдена");
            }
        }
        log.info("Обновлена вещь id={}", item.getId());

        return updItem;
    }

    @Override
    public Item getItemById(Integer itemId) {
        Item item = null;
        for (Item existedItem : itemStorage) {
            if (itemId.equals(existedItem.getId())) {
                item = existedItem;
            }
        }
        if (item == null) {
            log.warn("Вещь не найдена");
            throw new NotFoundException("Такая вещь не найдена");
        }
        log.info("Вызвана вещь id ={}", itemId);

        return item;
    }

    @Override
    public List<Item> getItemsForUser(Integer userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item existedItem : itemStorage) {
            if (userId.equals(existedItem.getOwner().getId())) {
                userItems.add(existedItem);
            }
        }
        log.info("Вызван список вещей для пользователя id ={}", userId);

        return userItems;
    }

    @Override
    public List<Item> searchItemByText(String text) {
        List<Item> matchItems = new ArrayList<>();
        for (Item existedItem : itemStorage) {
            if ((existedItem.getName().toLowerCase().contains(text)
                    || existedItem.getDescription().toLowerCase().contains(text))
                    && existedItem.getAvailable()) {
                matchItems.add(existedItem);
            }
        }
        log.info("Вызван список вещей по строке поиска \"{}\"", text);

        return matchItems;
    }
}
