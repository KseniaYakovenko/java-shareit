package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getItemById() throws Exception {
        long itemId = 1L;
        long userId = 1L;

        when(itemService.getById(itemId, userId)).thenReturn(new ItemInfoDto());

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(itemService, times(1)).getById(itemId, userId);
    }

    @Test
    void getAllItemsByUser() throws Exception {
        long userId = 1L;

        when(itemService.getUserItems(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).getUserItems(userId);
    }

    @Test
    void searchItems() throws Exception {
        long userId = 1L;
        String text = "text";

        when(itemService.search(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).search(text);
    }

    @Test
    void createItem() throws Exception {
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Name");
        itemDto.setAvailable(true);
        itemDto.setDescription("Description");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String itemJson = objectMapper.writeValueAsString(itemDto);

        when(itemService.create(itemDto, userId)).thenReturn(new ItemDto());

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(itemService, times(1)).create(itemDto, userId);
    }

    @Test
    void commentItem() throws Exception {
        long itemId = 1L;
        long userId = 1L;
        CommentRequestDto comment = new CommentRequestDto();
        comment.setText("Text");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String commentJson = objectMapper.writeValueAsString(comment);

        when(itemService.createComment(comment, userId, itemId)).thenReturn(new CommentDto());

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(itemService, times(1)).createComment(comment, userId, itemId);
    }

    @Test
    void editItem() throws Exception {
        long itemId = 1L;
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Name");
        itemDto.setAvailable(true);
        itemDto.setDescription("Description");
        itemDto.setId(itemId);
        Map<String, Object> updates = new HashMap<>();

        when(itemService.update(itemDto, userId)).thenReturn(itemDto);

        mockMvc.perform(patch("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }
}