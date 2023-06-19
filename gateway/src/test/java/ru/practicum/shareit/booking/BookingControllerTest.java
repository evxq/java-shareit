package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    private Booking booking;
    private BookingDto bookingDto;
    private BookItemRequestDto bookItemRequestDto;

    @BeforeEach
    void setup() {
        User user = new User(1, "name", "e@mail.ya");
        Item item = new Item(1, "name", "desc", true);
        booking = new Booking(1,
                LocalDateTime.now().plusHours(1).plusNanos(1),
                LocalDateTime.now().plusHours(2).plusNanos(1),
                item, user, BookingStatus.WAITING);
        bookingDto = BookingMapper.toBookingDto(booking);
        bookItemRequestDto = new BookItemRequestDto(
                item.getId(),
                LocalDateTime.now().plusHours(1).plusNanos(1),
                LocalDateTime.now().plusHours(2).plusNanos(1));
    }

    @SneakyThrows
    @Test
    void createBooking_returnBooking() {
        ResponseEntity<Object> entity = new ResponseEntity<>(bookItemRequestDto, HttpStatus.OK);
        when(bookingClient.bookItem(anyLong(), any())).thenReturn(entity);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(bookItemRequestDto))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId", Matchers.is(bookItemRequestDto.getItemId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", Matchers.is(bookItemRequestDto.getStart().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end", Matchers.is(bookItemRequestDto.getEnd().toString()), LocalDateTime.class))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookItemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void responseToBooking_returnBooking() {
        bookingDto.setStatus(BookingStatus.APPROVED);
        ResponseEntity<Object> entity = new ResponseEntity<>(bookingDto, HttpStatus.OK);
        when(bookingClient.responseToBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(entity);

        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDto.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", Matchers.is(bookingDto.getStart().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end", Matchers.is(bookingDto.getEnd().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item", Matchers.is(bookingDto.getItem()), Item.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker", Matchers.is(bookingDto.getBooker()), User.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(bookingDto.getStatus().toString()), BookingStatus.class))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getBookingById_returnBooking() {
        ResponseEntity<Object> entity = new ResponseEntity<>(bookingDto, HttpStatus.OK);
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(entity);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDto.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", Matchers.is(bookingDto.getStart().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end", Matchers.is(bookingDto.getEnd().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item", Matchers.is(bookingDto.getItem()), Item.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker", Matchers.is(bookingDto.getBooker()), User.class))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getBookingsForUser_returnBookingList() {
        ResponseEntity<Object> entity = new ResponseEntity<>(List.of(bookingDto), HttpStatus.OK);
        when(bookingClient.getBookings(anyLong(), any(), anyInt(), anyInt())).thenReturn(entity);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(bookingDto.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start", Matchers.is(bookingDto.getStart().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end", Matchers.is(bookingDto.getEnd().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item", Matchers.is(bookingDto.getItem()), Item.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker", Matchers.is(bookingDto.getBooker()), User.class));

        verify(bookingClient).getBookings(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getBookingsForOwner_returnBookingList() {
        ResponseEntity<Object> entity = new ResponseEntity<>(List.of(bookingDto), HttpStatus.OK);
        when(bookingClient.getBookingsForOwner(anyLong(), any(), anyInt(), anyInt())).thenReturn(entity);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(bookingDto.getId()), Integer.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].start", Matchers.is(bookingDto.getStart().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].end", Matchers.is(bookingDto.getEnd().toString()), LocalDateTime.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item", Matchers.is(bookingDto.getItem()), Item.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker", Matchers.is(bookingDto.getBooker()), User.class));

        verify(bookingClient).getBookingsForOwner(anyLong(), any(), anyInt(), anyInt());
    }

}
