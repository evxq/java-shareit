package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    //    private final ItemDao itemDao;
//    private final UserDao userDao;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(Integer userId, ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()
                || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
            log.warn("Недостаточно данных для создания вещи");
            throw new ValidationException("Недостаточно данных для создания вещи");
        }
        Item item = ItemMapper.toItem(itemDto);
//        item.setOwner(userDao.getUserById(userId));
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден");
                    return new NotFoundException("Такой пользователь не найден");
                }));
        Item newItem = itemRepository.save(item);
        log.info("Создана вещь id={}", newItem.getId());
//        return itemDao.addItem(item);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        Item existedItem = itemRepository.getReferenceById(itemId);
        if (!existedItem.getOwner().getId().equals(userId)) {
            log.warn("Объект принадлежит другому пользователю");
            throw new NotFoundException("Объект принадлежит другому пользователю");
        }
        if (itemDto.getName() != null) {                                // ОБНОВЛЯТЬ ТОЛЬКО ИМЯ
            existedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {                         // ОБНОВЛЯТЬ ТОЛЬКО ОПИСАНИЕ
            existedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {                           // ОБНОВЛЯТЬ ТОЛЬКО ДОСТУПНОСТЬ
            existedItem.setIsAvailable(itemDto.getAvailable());
        }
        Item updItem = itemRepository.save(existedItem);
        log.info("Обновлена вещь id={}", itemId);
//        return itemDao.updateItem(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(updItem);
    }

    @Override
    public ItemDtoBooking getItemById(Integer itemId) {
//        return itemDao.getItemById(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.warn("Объект не найден");
                    return new NotFoundException("Такой объект не найден");
                });
        log.info("Вызвана вещь id ={}", itemId);

        return setBookingsToItem(item);
    }

    @Override
    public List<ItemDtoBooking> getItemsForUser(Integer userId) {                         // Добавление дат бронирования при просмотре вещей
        log.info("Вызван список вещей для пользователя id ={}", userId);
//        List<Item> userItems = itemRepository.findAllByOwnerId(userId);
//        return itemDao.getItemsForUser(userId);
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(this::setBookingsToItem)
                .collect(Collectors.toList());
    }

    private ItemDtoBooking setBookingsToItem(Item item) {
        ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item);
        List<Booking> lastBooking = bookingRepository.findLastBookingForItem(item.getId(), LocalDateTime.now());
        if (!lastBooking.isEmpty()) {
            BookingDto lastBookingDto = BookingMapper.toBookingDto(lastBooking.get(0));
            itemDtoBooking.setLastBooking(lastBookingDto);
        }
        List<Booking> nextBooking = bookingRepository.findNextBookingForItem(item.getId(), LocalDateTime.now());
        if (!nextBooking.isEmpty()) {
            BookingDto nextBookingDto = BookingMapper.toBookingDto(nextBooking.get(0));
            itemDtoBooking.setNextBooking(nextBookingDto);
        }
        List<CommentDto> commentDtoList = commentRepository.findAllByItemId(item.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDtoBooking.setComments(commentDtoList);
        return itemDtoBooking;
    }

    @Override
    public List<ItemDto> searchItemByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.warn("Вызван поиск вещей для пустой строки");
            return new ArrayList<>();
        }
        log.info("Вызван список вещей по строке поиска \"{}\"", text);
//        return itemDao.searchItemByText(text.toLowerCase());
        return itemRepository.findAllByTextContaining(text.toLowerCase())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Integer userId, Integer itemId, CommentDto commentDto) {
        List<Booking> booking = bookingRepository.findBookingByUserAndItem(itemId, userId, LocalDateTime.now());
        if (booking.isEmpty()) {
            log.info("Пользователь не может оставить комментарий для объекта, который не использовал");
            throw new ValidationException("Пользователь не может оставить комментарий для объекта, который не использовал");
        } else {
            Comment comment = CommentMapper.toComment(commentDto);
            comment.setItem(booking.get(0).getItem());
            comment.setAuthor(booking.get(0).getBooker());

            return CommentMapper.toCommentDto(commentRepository.save(comment));
        }
    }

}
