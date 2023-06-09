package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private User user1;
    private User user2;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setup() {
        user1 = new User(1, "name", "e@mail.ya");
        user2 = new User(2, "name2", "e2@mail.ya");
        item = new Item(1, "name", "desc", true);
        item.setOwner(user1);
        booking = new Booking(1,
                LocalDateTime.now().plus(2, ChronoUnit.HOURS),
                LocalDateTime.now().plus(3, ChronoUnit.HOURS),
                item, user2, BookingStatus.WAITING);
        bookingDto = BookingMapper.toBookingDto(booking);
    }

    @Test
    void createBooking_returnBooking() {
        BookingItemDto bookingItemDto = BookingMapper.toBookingItemDto(booking);
        when(userService.getUserById(anyInt())).thenReturn(user2);
        when(itemService.getItemById(any())).thenReturn(item);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto newBookingDto = bookingService.createBooking(1, bookingItemDto);

        assertEquals(bookingDto, newBookingDto);
        verify(bookingRepository).save(booking);
    }

    @Test
    void responseToBooking_returnApprovedBooking() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDto newBookingDto = bookingService.responseToBooking(1, 1, true);
        booking.setStatus(BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        assertEquals(bookingDto, newBookingDto);
        verify(bookingRepository).save(booking);
    }

    @Test
    void getBookingById_returnBookingDto() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(userService.getUserById(anyInt())).thenReturn(user1);

        BookingDto newBookingDto = bookingService.getBookingById(1, 1);

        assertEquals(bookingDto, newBookingDto);
    }

    @Test
    void getBookingsForUser_returnBookingsList() {
        int from = 0;
        int size = 5;
        List<Booking> bookingList = List.of(booking);
        PageRequest page = PageRequest.of(from, size);
        Page<Booking> bookingPage = new PageImpl<>(bookingList);

        when(userService.getUserById(anyInt())).thenReturn(user1);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(), any(), any(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyInt(), any(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(anyInt(), any(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), eq(page))).thenReturn(bookingPage);

        List<BookingDto> allListBookingDto = bookingService.getBookingsForUser(1, "ALL", from, size);
        List<BookingDto> currentListBookingDto = bookingService.getBookingsForUser(1, "CURRENT", from, size);
        List<BookingDto> pastListBookingDto = bookingService.getBookingsForUser(1, "PAST", from, size);
        List<BookingDto> futureListBookingDto = bookingService.getBookingsForUser(1, "FUTURE", from, size);
        List<BookingDto> statusListBookingDto = bookingService.getBookingsForUser(1, "WAITING", from, size);

        List<BookingDto> listBookingDto = bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        assertEquals(allListBookingDto, listBookingDto);
        assertEquals(currentListBookingDto, listBookingDto);
        assertEquals(pastListBookingDto, listBookingDto);
        assertEquals(futureListBookingDto, listBookingDto);
        assertEquals(statusListBookingDto, listBookingDto);
    }

    @Test
    void getBookingsForOwner_returnBookingsList() {
        int from = 0;
        int size = 5;
        List<Booking> bookingList = List.of(booking);
        PageRequest page = PageRequest.of(from, size);
        Page<Booking> bookingPage = new PageImpl<>(bookingList);

        when(userService.getUserById(anyInt())).thenReturn(user1);
        when(bookingRepository.getBookingsForOwner(anyInt(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.getBookingsForOwnerCurrent(anyInt(), any(), any(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.getBookingsForOwnerPast(anyInt(), any(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.getBookingsForOwnerFuture(anyInt(), any(), eq(page))).thenReturn(bookingPage);
        when(bookingRepository.getBookingsForOwnerByStatus(anyInt(), any(), eq(page))).thenReturn(bookingPage);

        List<BookingDto> allListBookingDto = bookingService.getBookingsForOwner(1, "ALL", from, size);
        List<BookingDto> currentListBookingDto = bookingService.getBookingsForOwner(1, "CURRENT", from, size);
        List<BookingDto> pastListBookingDto = bookingService.getBookingsForOwner(1, "PAST", from, size);
        List<BookingDto> futureListBookingDto = bookingService.getBookingsForOwner(1, "FUTURE", from, size);
        List<BookingDto> statusListBookingDto = bookingService.getBookingsForOwner(1, "WAITING", from, size);

        List<BookingDto> listBookingDto = bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());

        assertEquals(allListBookingDto, listBookingDto);
        assertEquals(currentListBookingDto, listBookingDto);
        assertEquals(pastListBookingDto, listBookingDto);
        assertEquals(futureListBookingDto, listBookingDto);
        assertEquals(statusListBookingDto, listBookingDto);
    }

}