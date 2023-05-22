package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public Booking createBooking(Integer userId, BookingDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().equals(bookingDto.getEnd())) {
            log.warn("Даты бронирования не корректны");
            throw new ValidationException("Даты бронирования не корректны");
        }
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (item.getOwner().equals(user)) {
            log.warn("Пользователь не может забронировать собственный предмет");
            throw new NotFoundException("Пользователь не может забронировать собственный предмет");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || !item.getIsAvailable()) {
            log.warn("Объект не доступен для бронирования");
            throw new ValidationException("Объект не доступен для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking newBooking = bookingRepository.save(booking);
        log.info("Создано бронирование id={}", newBooking.getId());

        return newBooking;
    }

    @Override
    public Booking responseToBooking(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = checkBookingForExist(bookingId);
        Integer ownerId = booking.getItem().getOwner().getId();
        if (!ownerId.equals(userId)) {
            log.warn("id владельца объекта не совпадают");
            throw new NotFoundException("Только владелец объекта может подтверждать бронирование");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            log.warn("Повторное подтверждение");
            throw new ValidationException("Бронирование уже подтверждено");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            log.info("Бронирование id={} подтверждено", booking.getId());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            log.info("Бронирование id={} отклонено", booking.getId());
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Integer userId, Integer bookingId) {
        Booking booking = checkBookingForExist(bookingId);
        userService.getUserById(userId);
        Integer ownerId = booking.getItem().getOwner().getId();
        Integer bookerId = booking.getBooker().getId();
        if (!ownerId.equals(userId) && !bookerId.equals(userId)) {
            log.warn("id пользователя не соответствует участникам бронирования");
            throw new NotFoundException("Только букер или владелец объекта может просматривать бронирование");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsForUser(Integer userId, String state) {
        userService.getUserById(userId);
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
            case "REJECTED":
                userBookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(state));
                break;
            default:
                log.warn("Некорректный статус бронирования");
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        log.info("Получен список бронирований для пользователя id={} по условию {}", userId, state);

        return userBookings;
    }

    @Override
    public List<Booking> getBookingsForOwner(Integer userId, String state) {
        userService.getUserById(userId);
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
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        log.info("Получен список бронирований для владельца id={} по условию {}", userId, state);

        return ownerBookings;
    }

    private Booking checkBookingForExist(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Бронирование не найдено");
                    throw new NotFoundException("Такое бронирование не найдено");
                });
    }

}
