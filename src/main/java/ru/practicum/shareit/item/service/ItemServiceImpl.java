package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public Item addItem(Integer userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
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
        return itemDao.searchItemByText(text.toLowerCase());
    }

}
