package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getById() throws Exception {
        long userId = 1L;
        when(userService.getById(userId)).thenReturn(new UserDto());

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).getAll();
    }

    @Test
    void create() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Name");
        userDto.setEmail("email@mail.ru");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(userDto);

        when(userService.create(any())).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(userService, times(1)).create(userDto);
    }

    @Test
    void update() throws Exception {
        UserDto userDto = new UserDto();
        long userId = 1L;
        userDto.setId(userId);

        when(userService.update(any())).thenReturn(userDto);

        mockMvc.perform(patch("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(userService, times(1)).update(userDto);
    }

    @Test
    void deleteUser() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/users/" + userId)
                        .content("{}"))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void notFound() throws Exception {
        long userId = 100L;

        when(userService.getById(userId)).thenThrow(new NotFoundException("message"));

        mockMvc.perform(get("/users" + userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void bagRequestAccess() throws Exception {
        long userId = 100L;

        when(userService.getById(userId)).thenThrow(new AccessDeniedException("message"));

        mockMvc.perform(get("/users" + userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());
    }
}