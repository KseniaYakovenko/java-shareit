package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
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
        return toUserDto(userRepository.save(dtoToUser(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto) {

        Long id = userDto.getId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь " + id + " не найден"));

        if (userDto.getEmail() == null) {
            userDto.setEmail(user.getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(user.getName());
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(user.getName());
        }
        return toUserDto(userRepository.save(dtoToUser(userDto)));
    }

    @Override
    public UserDto getById(long userId) {
        return toUserDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Нет пользователя с id = " + userId)));
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserDtoMapper::toUserDto).collect(Collectors.toList());
    }
}
