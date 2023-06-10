package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
class ItemRequestServiceIntTest {

    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private UserDto userDto1;
    private UserDto userDto2;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        userDto1 = userService.createUser(
                UserDto.builder().name("name").email("e@mail.com").build());
        userDto2 = userService.createUser(
                UserDto.builder().name("name2").email("e2@mail.com").build());
        itemRequestDto = itemRequestService.addRequest(userDto1.getId(),
                ItemRequestMapper.toItemRequestDto(
                        new ItemRequest(1, "request", LocalDateTime.MIN, userDto1.getId())));
    }

    @Test
    void addRequest_returnRequestDto() {
        TypedQuery<ItemRequest> query = em.createQuery("SELECT r FROM ItemRequest r WHERE r.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId()).getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(itemRequestDto.getCreated()));
        assertThat(itemRequest.getRequesterId(), equalTo(itemRequestDto.getRequesterId()));
    }

    @Test
    void getMyRequests_returnRequestDtoList() {
        List<ItemRequestDto> itemRequestList = itemRequestService.getMyRequests(userDto1.getId());

        assertThat(itemRequestList, hasSize(1));
        for (ItemRequestDto item : itemRequestList) {
            assertThat(itemRequestList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(item.getDescription())),
                    hasProperty("created", equalTo(item.getCreated())),
                    hasProperty("requesterId", equalTo(item.getRequesterId()))
            )));
        }
    }

    @Test
    void getRequestById_returnRequestDto() {
        ItemRequestDto itemRequestById = itemRequestService.getRequestById(userDto1.getId(), itemRequestDto.getId());
        TypedQuery<ItemRequest> query = em.createQuery("SELECT r FROM ItemRequest r WHERE r.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId()).getSingleResult();

        assertThat(itemRequestById.getId(), notNullValue());
        assertThat(itemRequestById.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestById.getCreated(), equalTo(itemRequest.getCreated()));
        assertThat(itemRequestById.getRequesterId(), equalTo(itemRequest.getRequesterId()));
    }

    @Test
    void getAllRequests_returnRequestDtoList() {
        int from = 0;
        int size = 5;

        List<ItemRequestDto> requestList = itemRequestService.getAllRequests(userDto2.getId(), from, size);

        assertThat(requestList, hasSize(1));
        for (ItemRequestDto request : requestList) {
            assertThat(requestList, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(request.getDescription())),
                    hasProperty("created", equalTo(request.getCreated())),
                    hasProperty("requesterId", equalTo(request.getRequesterId()))
            )));
        }
    }

}