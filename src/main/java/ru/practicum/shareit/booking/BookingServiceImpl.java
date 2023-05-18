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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден");
                    return new NotFoundException("Такой пользователь не найден");
                });
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
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Бронирование не найдено");
                    return new NotFoundException("Такое бронирование не найдено");
                });
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
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("Бронирование не найдено");
                    return new NotFoundException("Такое бронирование не найдено");
                });
        Integer ownerId = booking.getItem().getOwner().getId();
        Integer bookerId = booking.getBooker().getId();
        if (!ownerId.equals(userId) || !bookerId.equals(userId)) {
            log.warn("id пользователя не соответствует участникам бронирования");
            throw new ValidationException("Только букер или владелец объекта может просматривать бронирование");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsForUser(Integer userId, String state) {
        return null;
    }

    @Override
    public List<Booking> getBookingsForItemsByUser(Integer userId, String state) {
        return null;
    }

}
