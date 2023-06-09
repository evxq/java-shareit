package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private User user1;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setup() {
        user1 = new User(1, "name", "e@mail.ya");
        itemRequest = new ItemRequest(1, "request", LocalDateTime.MIN, 1);
        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Test
    void addRequest_returnRequest() {
        when(userService.getUserById(anyInt())).thenReturn(user1);
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequestDto newItemRequestDto = itemRequestService.addRequest(1, itemRequestDto);
        itemRequestDto.setCreated(LocalDateTime.MIN);

        assertEquals(itemRequestDto, newItemRequestDto);
        verify(itemRequestRepository).save(itemRequest);
    }

    @Test
    void getMyRequests() {
        itemRequestDto.setItems(List.of());
        List<ItemRequestDto> itemRequestList = List.of(itemRequestDto);
        when(userService.getUserById(anyInt())).thenReturn(user1);
        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(anyInt())).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> newItemRequestList = itemRequestService.getMyRequests(1);

        assertEquals(newItemRequestList, itemRequestList);
    }

    @Test
    void getRequestById() {
        itemRequestDto.setItems(List.of());
        when(userService.getUserById(anyInt())).thenReturn(user1);
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));

        ItemRequestDto newItemRequestDto = itemRequestService.getRequestById(1, 1);

        assertEquals(itemRequestDto, newItemRequestDto);
    }

    @Test
    void getAllRequests() {
        int from = 0;
        int size = 5;
        itemRequestDto.setItems(List.of());
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
        PageRequest page = PageRequest.of(from, size);
        List<ItemRequest> itemRequestList = List.of(itemRequest);
        Page<ItemRequest> itemRequestPage = new PageImpl<>(itemRequestList);
        when(userService.getUserById(anyInt())).thenReturn(user1);
        when(itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(anyInt(), eq(page)))
                .thenReturn(itemRequestPage);

        List<ItemRequestDto> newItemRequestList = itemRequestService.getAllRequests(1, from, size);

        assertEquals(newItemRequestList, itemRequestDtoList);
    }

}