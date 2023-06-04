package ru.practicum.shareit.item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .requestId(item.getRequestId()).build();
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequestId());
    }

    public static ItemDtoBooking toItemDtoBooking(Item item) {
        return ItemDtoBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .requestId(item.getRequestId()).build();
    }

    public static Item toItem(ItemDtoBooking itemDtoBooking) {
        return new Item(
                itemDtoBooking.getId(),
                itemDtoBooking.getName(),
                itemDtoBooking.getDescription(),
                itemDtoBooking.getAvailable(),
                itemDtoBooking.getRequestId());
    }

}
