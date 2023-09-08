package ru.practicum.ewm_service.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_service.user.dto.NewUserDto;
import ru.practicum.ewm_service.user.dto.UserDto;
import ru.practicum.ewm_service.user.dto.UserDtoRate;
import ru.practicum.ewm_service.user.dto.UserDtoShort;
import ru.practicum.ewm_service.user.model.User;

@RequiredArgsConstructor
@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserDtoShort toUserDtoShort(User user) {
        return UserDtoShort.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(NewUserDto newUserDto) {
        return User.builder()
                .name(newUserDto.getName())
                .email(newUserDto.getEmail())
                .build();
    }

    public static UserDtoRate toUserDtoRate(User user) {
        return UserDtoRate.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
