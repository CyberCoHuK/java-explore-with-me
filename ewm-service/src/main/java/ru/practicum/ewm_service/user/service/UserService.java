package ru.practicum.ewm_service.user.service;

import ru.practicum.ewm_service.user.dto.UserDto;
import ru.practicum.ewm_service.user.dto.UserDtoShort;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<UserDto> getAllUsers(List<Long> ids, int from, int size);

    UserDto createUser(UserDtoShort userDtoShort);

    void deleteUserById(long userId);
}
