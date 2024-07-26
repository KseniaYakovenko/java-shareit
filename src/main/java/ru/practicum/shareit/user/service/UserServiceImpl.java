package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserDtoMapper.dtoToUser;
import static ru.practicum.shareit.user.dto.UserDtoMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        return toUserDto(userRepository.create(dtoToUser(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto) {
        return toUserDto(userRepository.update(dtoToUser(userDto)));
    }

    @Override
    public UserDto getById(long userId) {
        return toUserDto(userRepository.getById(userId));
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream().map(UserDtoMapper::toUserDto).collect(Collectors.toList());
    }
}
