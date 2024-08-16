package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.request.service.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RequestController.class)
public class RequestControllerTest {
    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll() throws Exception {
        long userId = 1L;
        when(requestService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(requestService, times(1)).findAll();
    }

    @Test
    void getItemRequestById() throws Exception {
        long userId = 1L;
        long requestId = 1L;
        when(requestService.getByRequestId(requestId, userId)).thenReturn(new RequestInfoDto());

        mockMvc.perform(get("/requests/" + requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(requestService, times(1)).getByRequestId(requestId, userId);
    }

    @Test
    void getAllByUser() throws Exception {
        long userId = 1L;
        long requestId = 1L;
        when(requestService.findAllRequestsByUserId(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(requestService, times(1)).findAllRequestsByUserId(userId);

    }

    @Test
    void createItemRequest() throws Exception {
        long userId = 1L;

        RequestDto requestDto = new RequestDto();

        requestDto.setDescription("Description");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String itemRequestJson = objectMapper.writeValueAsString(requestDto);

        when(requestService.create(requestDto, userId)).thenReturn(new RequestInfoDto());

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(requestService, times(1)).create(eq(requestDto), eq(userId));
    }
}