package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequestDto addRequest(Integer userId, ItemRequestDto itemRequestDto) {
        userService.getUserById(userId);
        itemRequestDto.setRequesterId(userId);
        ItemRequest savedRequest = itemRequestRepository
                .save(ItemRequestMapper.toItemRequest(itemRequestDto));
        log.info("Создан запрос id={}", savedRequest.getId());

        return ItemRequestMapper.toItemRequestDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getMyRequests(Integer userId) {         // ДОБАВИТЬ СПИСОК ВЕЩЕЙ
        userService.getUserById(userId);
        log.info("Получен список запросов для пользователя id={}", userId);

        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Integer userId, Integer requestId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer userId, int from, int size) {
        return null;
    }

}
