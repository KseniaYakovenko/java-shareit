package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.Marker;

import java.util.List;

import static ru.practicum.shareit.user.controller.ErrorResponse.getErrorResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> getAllUser() {
        return userService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @RequestBody @Validated(Marker.OnCreate.class) UserDto userDto) {
        log.info("Create user: {} - Started", userDto);
        UserDto createdUser = userService.create(userDto);
        log.info("Create user: {} - Finished", createdUser);
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId,
                              @RequestBody @Validated(Marker.OnUpdate.class) UserDto userDto) {
        userDto.setId(userId);
        log.info("Update user: {} - Started", userDto);
        UserDto updatedUser = userService.update(userDto);
        log.info("Update user: {} - Finished", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        log.info("Getting user: {} - Started", userId);
        UserDto userDto = userService.getById(userId);
        log.info("Getting user: {} - Finished", userId);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("Deleting user: {} - Started", userId);
        userService.delete(userId);
        log.info("Deleting user: {} - Finished", userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e) {
        return getErrorResponse(e, log);
    }
}
