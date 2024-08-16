package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Transactional
@RequiredArgsConstructor
@SpringBootTest
public class UserServiceImplITest {
    @Autowired
    private EntityManager em;
    @Autowired
    private UserService userService;
    private User user;


    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setName("UserName");
        user.setEmail("user@email.ru");
        em.persist(user);

    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long createdUserId = createdUser.getId();

        Assertions.assertNotEquals(createdUserId, null);
        Assertions.assertEquals(name, createdUser.getName());
        Assertions.assertEquals(email, createdUser.getEmail());
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest2@email.ru";
        String newName = "UserNewName";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long createdUserId = createdUser.getId();
        createdUser.setName(newName);
        UserDto updatedUser = userService.update(createdUser);

        Assertions.assertEquals(createdUserId, updatedUser.getId());
        Assertions.assertEquals(newName, updatedUser.getName());
        Assertions.assertEquals(email, updatedUser.getEmail());
    }

    @Test
    void deleteUser() {
        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest2@email.ru";
        String newName = "UserNewName";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long createdUserId = createdUser.getId();
        createdUser.setName(newName);
        userService.delete(createdUserId);

        Assertions.assertThrows(NotFoundException.class, () -> userService.getById(createdUserId));
    }

    @Test
    void getById() {
        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long createdUserId = createdUser.getId();

        userService.getById(createdUserId);
        Assertions.assertNotEquals(createdUserId, null);
        Assertions.assertEquals(name, createdUser.getName());
        Assertions.assertEquals(email, createdUser.getEmail());
    }

    @Test
    void getAll() {
        UserDto userDto = new UserDto();
        String name = "UserName";
        String email = "userTest@email.ru";
        userDto.setName(name);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long createdUserId = createdUser.getId();

        userService.getAll();
        Assertions.assertNotEquals(createdUserId, null);
        Assertions.assertEquals(name, createdUser.getName());
        Assertions.assertEquals(email, createdUser.getEmail());
    }
}
