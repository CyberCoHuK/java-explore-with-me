package ru.practicum.ewm_service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.ewm_service.exceptions.ObjectNotFoundException;
import ru.practicum.ewm_service.user.dto.UserDto;
import ru.practicum.ewm_service.user.dto.UserDtoShort;
import ru.practicum.ewm_service.user.mapper.UserMapper;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        Page<User> answer = CollectionUtils.isEmpty(ids) ? userRepository.findAll(page) :
                userRepository.findAllByIdIn(ids, page);
        return answer.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto createUser(UserDtoShort userDtoShort) {
        User user = userRepository.save(UserMapper.toUser(userDtoShort));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        userRepository.delete(user);
    }
}
