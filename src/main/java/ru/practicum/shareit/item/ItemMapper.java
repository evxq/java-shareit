package ru.practicum.shareit.item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable()).build();
    }

    public static Item toItem(ItemDto itemDto) {
        /*return Item.builder()
                .id(itemDto.getItemDtoId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable()).build();*/

        return new Item(
                itemDto.getItemDtoId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }

}
