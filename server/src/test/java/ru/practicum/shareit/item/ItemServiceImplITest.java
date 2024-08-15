package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@SpringBootTest
public class ItemServiceImplITest {
    @Autowired
    private EntityManager em;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    private User user;


    @Test
    void createItem() {
        ItemDto itemDto = new ItemDto();
        String name = "itemName";
        String description = "itemDesc";
        Boolean available = true;
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        UserDto userDto = new UserDto();
        String userName = "UserName";
        String email = "userTest30@email.ru";
        userDto.setName(userName);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long ownerId = createdUser.getId();
        ItemDto createdItem = itemService.create(itemDto, ownerId);

        Assertions.assertNotEquals(createdItem.getId(), null);
        Assertions.assertEquals(name, createdItem.getName());
        Assertions.assertEquals(description, createdItem.getDescription());
    }

    @Test
    void updateItem() {
        ItemDto itemDto = new ItemDto();
        String name = "itemName";
        String description = "itemDesc";
        Boolean available = true;
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        UserDto userDto = new UserDto();
        String userName = "UserName";
        String email = "userTest40@email.ru";
        userDto.setName(userName);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long ownerId = createdUser.getId();
        ItemDto createdItem = itemService.create(itemDto, ownerId);

        createdItem.setAvailable(false);

        ItemDto updatedItem = itemService.update(createdItem, ownerId);


        Assertions.assertEquals(false, updatedItem.getAvailable());
        Assertions.assertEquals(description, updatedItem.getDescription());
    }

    @Test
    void getById() {
        ItemDto itemDto = new ItemDto();
        String name = "itemName";
        String description = "itemDesc";
        Boolean available = true;
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        UserDto userDto = new UserDto();
        String userName = "UserName";
        String email = "userTest50@email.ru";
        userDto.setName(userName);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long ownerId = createdUser.getId();
        ItemDto createdItem = itemService.create(itemDto, ownerId);

        Long itemId = createdItem.getId();

        ItemInfoDto item = itemService.getById(itemId, ownerId);

        Assertions.assertEquals(itemId, item.getId());
        Assertions.assertEquals(name, item.getName());
    }

    @Test
    void getUserItem() {
        ItemDto itemDto = new ItemDto();
        String name = "itemName";
        String description = "itemDesc";
        Boolean available = true;
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        UserDto userDto = new UserDto();
        String userName = "UserName";
        String email = "userTest60@email.ru";
        userDto.setName(userName);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long ownerId = createdUser.getId();
        ItemDto createdItem = itemService.create(itemDto, ownerId);
        Long itemId = createdItem.getId();

        List<ItemInfoDto> items = itemService.getUserItems(ownerId);
        System.out.println(items);

        Assertions.assertTrue(items.size() > 0);
        Assertions.assertEquals(items.get(0).getId(), itemId);
    }


    @Test
    void search() {
        ItemDto itemDto = new ItemDto();
        String name = "text";
        String description = "itemDesc";
        Boolean available = true;
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        UserDto userDto = new UserDto();
        String userName = "UserName";
        String email = "userTest90@email.ru";
        userDto.setName(userName);
        userDto.setEmail(email);

        UserDto createdUser = userService.create(userDto);
        Long ownerId = createdUser.getId();
        ItemDto createdItem = itemService.create(itemDto, ownerId);
        Long itemId = createdItem.getId();

        List<ItemDto> items = itemService.search("text");

        Assertions.assertTrue(items.size() > 0);
        Assertions.assertEquals(items.get(0).getId(), itemId);
    }
}
