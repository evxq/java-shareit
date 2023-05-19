package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

//    private final ItemDao itemDao;
//    private final UserDao userDao;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Item addItem(Integer userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (item.getName() == null || item.getName().isEmpty() || item.getDescription() == null || item.getIsAvailable() == null) {
            log.warn("Недостаточно данных для создания вещи");
            throw new ValidationException("Недостаточно данных для создания вещи");
        }
//        item.setOwner(userDao.getUserById(userId));
        item.setOwner(userRepository.getReferenceById(userId));
        log.info("Создана вещь id={}", item.getId());

//        return itemDao.addItem(item);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        if (itemDto.getItemDtoId() == null) {
            itemDto.setItemDtoId(itemId);
        }
        log.info("Обновлена вещь id={}", itemId);

//        return itemDao.updateItem(userId, ItemMapper.toItem(itemDto));
        return itemRepository.save(ItemMapper.toItem(itemDto));
    }

    @Override
    public Item getItemById(Integer itemId) {
        log.info("Вызвана вещь id ={}", itemId);

//        return itemDao.getItemById(itemId);
        return itemRepository.getReferenceById(itemId);
    }

    @Override
    public List<Item> getItemsForUser(Integer userId) {
        log.info("Вызван список вещей для пользователя id ={}", userId);

//        return itemDao.getItemsForUser(userId);
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<Item> searchItemByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.warn("Вызван поиск вещей для пустой строки");
            return new ArrayList<>();
        }
        log.info("Вызван список вещей по строке поиска \"{}\"", text);

//        return itemDao.searchItemByText(text.toLowerCase());
        return itemRepository.findAllByTextContaining(text.toLowerCase());
    }

}
