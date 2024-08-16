package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.user.dto.UserDtoMapper.dtoToUser;
import static ru.practicum.shareit.user.dto.UserDtoMapper.toUserDto;

@RequiredArgsConstructor
@SpringBootTest(classes = UserDtoMapper.class)
public class UserDtoMapperTest {

    @Test
    void toDto() {
        User user = new User();
        String name = "UserName";
        String email = "user@email.ru";
        Long id = 1L;
        user.setName(name);
        user.setEmail(email);
        user.setId(id);
        UserDto userDto = toUserDto(user);
        Assertions.assertEquals(id, userDto.getId());
        Assertions.assertEquals(name, userDto.getName());
        Assertions.assertEquals(email, userDto.getEmail());
    }

    @Test
    void toUser() {
        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "user@email.ru";
        Long id = 1L;
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setId(id);
        User user = dtoToUser(userDto);
        Assertions.assertEquals(id, user.getId());
        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(email, user.getEmail());
    }
}
