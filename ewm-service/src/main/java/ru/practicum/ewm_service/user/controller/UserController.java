package ru.practicum.ewm_service.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.user.dto.NewUserDto;
import ru.practicum.ewm_service.user.dto.UserDto;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Collection<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserDto newUserDto) {
        return userService.createUser(newUserDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}
