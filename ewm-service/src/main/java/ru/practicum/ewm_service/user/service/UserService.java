package ru.practicum.ewm_service.user.service;

import ru.practicum.ewm_service.user.dto.NewUserDto;
import ru.practicum.ewm_service.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<UserDto> getAllUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserDto newUserDto);

    void deleteUserById(long userId);
}
