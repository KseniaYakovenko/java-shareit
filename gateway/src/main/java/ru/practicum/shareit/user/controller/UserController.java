package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validator.Marker;

@Slf4j
@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("users")
public class UserController {
    private final UserClient userClient;

    @GetMapping()
    public ResponseEntity<Object> getAllUser() {
        return userClient.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createUser(
            @RequestBody @Validated(Marker.OnCreate.class) UserDto userDto) {
        log.info("Create user: {} - Started", userDto);
        ResponseEntity<Object> createdUser = userClient.create(userDto);
        log.info("Create user: {} - Finished", createdUser);
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @RequestBody @Validated(Marker.OnUpdate.class) UserDto userDto) {
        userDto.setId(userId);
        log.info("Update user: {} - Started", userDto);
        ResponseEntity<Object> updatedUser = userClient.update(userDto, userId);
        log.info("Update user: {} - Finished", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        log.info("Getting user: {} - Started", userId);
        ResponseEntity<Object> userDto = userClient.getById(userId);
        log.info("Getting user: {} - Finished", userId);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("Deleting user: {} - Started", userId);
        userClient.delete(userId);
        log.info("Deleting user: {} - Finished", userId);
    }
}
