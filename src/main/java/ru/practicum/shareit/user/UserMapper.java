package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

}
