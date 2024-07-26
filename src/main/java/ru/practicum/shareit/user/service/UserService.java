package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    UserDto getById(long userId);

    void delete(long userId);

    List<UserDto> getAll();
}
