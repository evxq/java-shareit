package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(Integer userId, BookingDto bookingDto) {
        User user = checkUserForExist(userId);
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> {
                    log.warn("Объект не найден");
                    return new NotFoundException("Такой объект не найден");
                });
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        log.info("Создано бронирование id={}", booking.getId());

        return booking;
    }

    @Override
    public Booking responseToBooking(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = checkBookingForExist(bookingId);
        Integer ownerId = booking.getItem().getOwner().getId();
        if (!ownerId.equals(userId)) {
            log.warn("id владельца объекта не совпадают");
            throw new ValidationException("Только владелец объекта может подтверждать бронирование");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            log.info("Бронирование подтверждено id={}", booking.getId());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            log.info("Бронирование отклонено id={}", booking.getId());
        }
        bookingRepository.save(booking);

        return booking;
    }

    @Override
    public Booking getBookingById(Integer userId, Integer bookingId) {
        Booking booking = checkBookingForExist(bookingId);
        Integer ownerId = booking.getItem().getOwner().getId();
        Integer bookerId = booking.getBooker().getId();
        if (!ownerId.equals(userId) || !bookerId.equals(userId)) {
            log.warn("id пользователя не соответствует участникам бронирования");
            throw new ValidationException("Только букер или владелец объекта может просматривать бронирование");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsForUser(Integer userId, String state) {             // Получение списка всех бронирований пользователя
        checkUserForExist(userId);
        List<Booking> userBookings;
        switch (state) {
            case "ALL":
                userBookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                userBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                userBookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                userBookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "WAITING":
                userBookings = bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId, "WAITING");
                break;
            case "REJECTED":
                userBookings = bookingRepository.findAllByBookerIdAndStatusIsOrderByStartDesc(userId, "REJECTED");
                break;
            default:
                log.warn("Некорректный статус бронирования");
                throw new ValidationException("Некорректный статус бронирования");
        }
        log.info("Получен список бронирований для пользователя id={} по условию {}", userId, state);

        return userBookings;
    }

    @Override
    public List<Booking> getBookingsForOwner(Integer userId, String state) {      // Получение списка бронирований для всех вещей пользователя-владельца
        checkUserForExist(userId);
        List<Booking> ownerBookings;
        switch (state) {
            case "ALL":
                ownerBookings = bookingRepository.getBookingsForOwner(userId);
                break;
            case "CURRENT":
                ownerBookings = bookingRepository.getBookingsForOwnerCurrent(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                ownerBookings = bookingRepository.getBookingsForOwnerPast(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                ownerBookings = bookingRepository.getBookingsForOwnerFuture(userId, LocalDateTime.now());
                break;
            case "WAITING":
            case "REJECTED":
                ownerBookings = bookingRepository.getBookingsForOwnerByStatus(userId, state);
                break;
            default:
                log.warn("Некорректный статус бронирования");
                throw new ValidationException("Некорректный статус бронирования");
        }
        log.info("Получен список бронирований для владельца id={} по условию {}", userId, state);

        return ownerBookings;
    }

    private User checkUserForExist(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден");
                    return new NotFoundException("Такой пользователь не найден");
                });
    }

    private Booking checkBookingForExist(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Бронирование не найдено");
                    return new NotFoundException("Такое бронирование не найдено");
                });
    }

}
