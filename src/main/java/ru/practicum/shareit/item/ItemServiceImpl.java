package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
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
        Item item = ItemMapper.toItem(itemDto);
        if (item.getName() == null || item.getName().isEmpty() || item.getDescription() == null || item.getIsAvailable() == null) {
            log.warn("Недостаточно данных для создания вещи");
            throw new ValidationException("Недостаточно данных для создания вещи");
        }
//        item.setOwner(userDao.getUserById(userId));
//        item.setOwner(userRepository.getReferenceById(userId));                       ////////// !!!!!!!!!!!!!!!!
        item.setOwner(userRepository.getById(userId));
        log.info("Создана вещь id={}", item.getId());
//        return itemDao.addItem(item);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        log.info("Обновлена вещь id={}", itemId);
//        return itemDao.updateItem(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDtoBooking getItemById(Integer itemId) {
        log.info("Вызвана вещь id ={}", itemId);
//        return itemDao.getItemById(itemId);
//        Item item = itemRepository.getReferenceById(itemId);                      !!!!!!!!!!!!!!!!!!!!
        Item item = itemRepository.getById(itemId);
        /*ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(itemRepository.getReferenceById(itemId));
        BookingDto lastBookingDto = BookingMapper.toBookingDto(bookingRepository.findLastBookingForItem(itemId, LocalDateTime.now()));
        BookingDto nextBookingDto = BookingMapper.toBookingDto(bookingRepository.findNextBookingForItem(itemId, LocalDateTime.now()));
        itemDtoBooking.setLastBooking(lastBookingDto);
        itemDtoBooking.setNextBooking(nextBookingDto);
        return itemDtoBooking;*/
        return setBookingsToItem(item);
    }

    @Override
    public List<ItemDtoBooking> getItemsForUser(Integer userId) {                         // Добавление дат бронирования при просмотре вещей
        log.info("Вызван список вещей для пользователя id ={}", userId);
        List<Item> userItems = itemRepository.findAllByOwnerId(userId);
//        return itemDao.getItemsForUser(userId);
        return userItems.stream()
                .map(item -> {
                    /*ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item);
                    BookingDto lastBookingDto = BookingMapper.toBookingDto(bookingRepository.findLastBookingForItem(item.getId(), LocalDateTime.now()));
                    BookingDto nextBookingDto = BookingMapper.toBookingDto(bookingRepository.findNextBookingForItem(item.getId(), LocalDateTime.now()));
                    itemDtoBooking.setLastBooking(lastBookingDto);
                    itemDtoBooking.setNextBooking(nextBookingDto);
                    return itemDtoBooking;*/
                    return setBookingsToItem(item);
                })
                .collect(Collectors.toList());
    }

    private ItemDtoBooking setBookingsToItem(Item item) {
        ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item);
        BookingDto lastBookingDto = BookingMapper.toBookingDto(bookingRepository.findLastBookingForItem(item.getId(), LocalDateTime.now()).get(0));
        BookingDto nextBookingDto = BookingMapper.toBookingDto(bookingRepository.findNextBookingForItem(item.getId(), LocalDateTime.now()).get(0));
        itemDtoBooking.setLastBooking(lastBookingDto);
        itemDtoBooking.setNextBooking(nextBookingDto);
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
