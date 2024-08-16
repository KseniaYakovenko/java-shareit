package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import static ru.practicum.shareit.item.dto.ItemDtoMapper.*;

@RequiredArgsConstructor
@SpringBootTest(classes = ItemDtoMapper.class)
public class ItemDtoMapperTest {

    @Test
    void toDto() {
        Item item = new Item();
        Long id = 1000L;
        String name = "UserName";
        String description = "user@email.ru";
        Boolean available = true;
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        ItemDto itemDto = toItemDto(item);
        Assertions.assertEquals(id, itemDto.getId());
        Assertions.assertEquals(name, itemDto.getName());
        Assertions.assertEquals(available, itemDto.getAvailable());
    }

    @Test
    void toItem() {
        ItemDto itemDto = new ItemDto();
        String name = "UserName";
        String description = "user@email.ru";
        Boolean available = false;
        Long id = 100L;
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setId(id);
        itemDto.setAvailable(available);
        Item item = dtoToItem(itemDto);
        Assertions.assertEquals(id, item.getId());
        Assertions.assertEquals(name, item.getName());
        Assertions.assertEquals(available, item.getAvailable());
    }

    @Test
    void toInfoDto() {
        Item item = new Item();
        Long id = 1000L;
        String name = "UserName";
        String description = "user@email.ru";
        Boolean available = true;
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        ItemInfoDto itemDto = toItemInfoDto(item);
        Assertions.assertEquals(id, itemDto.getId());
        Assertions.assertEquals(name, itemDto.getName());
        Assertions.assertEquals(available, itemDto.getAvailable());
    }
}
