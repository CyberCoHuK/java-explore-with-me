package ru.practicum.ewm_service.user.mapper;

import ru.practicum.ewm_service.user.dto.UserDto;
import ru.practicum.ewm_service.user.dto.UserDtoShort;
import ru.practicum.ewm_service.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDtoShort userDtoShort) {
        return User.builder()
                .name(userDtoShort.getName())
                .email(userDtoShort.getEmail())
                .build();
    }
}
