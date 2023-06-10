package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntTest {

    private final EntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private UserDto userDto1;
    private UserDto userDto2;
    private BookingDto bookingDto;
    private BookingItemDto bookingItemDto;

    @BeforeEach
    void setup() {
        userDto1 = userService.createUser(
                UserDto.builder().name("name").email("e@mail.com").build());
        userDto2 = userService.createUser(
                UserDto.builder().name("name2").email("e2@mail.com").build());
        ItemDto itemDto = itemService.addItem(
                userDto1.getId(), ItemDto.builder().name("name").description("desc").available(true).build());
        bookingItemDto = new BookingItemDto(1,
                LocalDateTime.now().plusNanos(100_000_000),
                LocalDateTime.now().plusNanos(200_000_000),
                itemDto.getId(), userDto2.getId(), "APPROVED");
        bookingDto = bookingService.createBooking(userDto2.getId(), bookingItemDto);
    }

    @Test
    void createBooking_returnBooking() {
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(bookingItemDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingItemDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(bookingItemDto.getItemId()));
        assertThat(booking.getBooker().getId(), equalTo(bookingItemDto.getBookerId()));
    }

    @Test
    void responseToBooking_returnApprovedBooking() {
        BookingDto approvedBookingDto = bookingService.responseToBooking(userDto1.getId(), bookingDto.getId(), true);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(approvedBookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(approvedBookingDto.getEnd()));
        assertThat(booking.getItem(), equalTo(approvedBookingDto.getItem()));
        assertThat(booking.getBooker(), equalTo(approvedBookingDto.getBooker()));
        assertThat(BookingStatus.APPROVED, equalTo(approvedBookingDto.getStatus()));
    }

    @Test
    void getBookingById_returnBookingDto() {
        BookingDto bookingById = bookingService.getBookingById(userDto1.getId(), bookingDto.getId());
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertThat(bookingById.getId(), notNullValue());
        assertThat(bookingById.getStart(), equalTo(booking.getStart()));
        assertThat(bookingById.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingById.getItem(), equalTo(booking.getItem()));
        assertThat(bookingById.getBooker(), equalTo(booking.getBooker()));
        assertThat(bookingById.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void getBookingsForUser_returnBookingsList() {
        int from = 0;
        int size = 5;

        List<BookingDto> allListBookingDto = bookingService.getBookingsForUser(userDto2.getId(), "ALL", from, size);
        List<BookingDto> futureListBookingDto = bookingService.getBookingsForUser(userDto2.getId(), "FUTURE", from, size);
        List<BookingDto> statusListBookingDto = bookingService.getBookingsForUser(userDto2.getId(), "WAITING", from, size);

        assertThat(allListBookingDto, hasSize(1));
        assertThat(statusListBookingDto, hasSize(1));
        assertThat(futureListBookingDto, hasSize(1));
        for (BookingDto booking : allListBookingDto) {
            assertThat(allListBookingDto, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", equalTo(booking.getItem())),
                    hasProperty("booker", equalTo(booking.getBooker()))
            )));
        }
    }

    @Test
    void getBookingsForOwner_returnBookingsList() {
        int from = 0;
        int size = 5;

        List<BookingDto> allListBookingDto = bookingService.getBookingsForOwner(userDto1.getId(), "ALL", from, size);
        List<BookingDto> futureListBookingDto = bookingService.getBookingsForOwner(userDto1.getId(), "FUTURE", from, size);
        List<BookingDto> statusListBookingDto = bookingService.getBookingsForOwner(userDto1.getId(), "WAITING", from, size);

        assertThat(allListBookingDto, hasSize(1));
        assertThat(statusListBookingDto, hasSize(1));
        assertThat(futureListBookingDto, hasSize(1));
        for (BookingDto booking : allListBookingDto) {
            assertThat(allListBookingDto, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", equalTo(booking.getItem())),
                    hasProperty("booker", equalTo(booking.getBooker()))
            )));
        }
    }

}