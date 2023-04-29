package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

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
        for (Item existedItem: itemStorage) {
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
                throw new NotFoundException("Такой вещь не найдена");
            }
        }
        log.info("Обновлена вещь id={}", item.getId());

        return updItem;
    }

    @Override
    public Item getItemDyId(Integer itemId) {
        return null;
    }

    @Override
    public List<Item> getItemsForUser() {
        return null;
    }

    @Override
    public List<Item> searchItemByText(String text) {
        return null;
    }
}
