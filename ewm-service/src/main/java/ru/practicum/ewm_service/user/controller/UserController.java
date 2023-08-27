package ru.practicum.ewm_service.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.user.dto.UserDto;
import ru.practicum.ewm_service.user.dto.UserDtoShort;
import ru.practicum.ewm_service.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDtoShort userDtoShort) {
        return userService.createUser(userDtoShort);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}
