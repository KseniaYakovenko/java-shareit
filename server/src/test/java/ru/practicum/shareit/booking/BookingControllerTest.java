package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controler.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncorrectActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getBookingsById() throws Exception {
        long bookingId = 1L;
        long userId = 1L;

        when(bookingService.getBookingByIdAndUserId(bookingId, userId)).thenReturn(new BookingDto());

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
        verify(bookingService, times(1)).getBookingByIdAndUserId(bookingId, userId);
    }

    @ParameterizedTest
    @EnumSource(BookingState.class)
    void getBookingsByUser(BookingState state) throws Exception {
        long userId = 2L;
        when(bookingService.getBookings(eq(userId), eq(state)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state.name()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getBookings(eq(userId), eq(state));
    }

    @ParameterizedTest
    @EnumSource(BookingState.class)
    void getBookingsByOwner(BookingState state) throws Exception {
        long userId = 3L;
        when(bookingService.getBookingsForOwner(eq(userId), eq(state)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state.name()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getBookingsForOwner(eq(userId), eq(state));
    }

    @Test
    void createBooking() throws Exception {
        long userId = 4L;
        long itemId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        BookingRequestDto bookingDto = new BookingRequestDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String bookingJson = objectMapper.writeValueAsString(bookingDto);

        when(bookingService.create(bookingDto, userId)).thenReturn(new BookingDto());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).create(refEq(bookingDto), eq(userId));
    }

    @Test
    void approveBooking() throws Exception {
        long userId = 5L;
        long bookingId = 2L;
        boolean approved = true;

        when(bookingService.update(bookingId, approved, userId)).thenReturn(new BookingDto());

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(bookingService, times(1)).update(bookingId, approved, userId);
    }

    @Test
    void notFound() throws Exception {
        long userId = 5L;
        long bookingId = 2L;
        boolean approved = true;

        when(bookingService.update(bookingId, approved, userId)).thenThrow(new NotFoundException("message"));

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"message\"}"));

        verify(bookingService, times(1)).update(bookingId, approved, userId);
    }

    @Test
    void badRequestUnavailableItem() throws Exception {
        long userId = 5L;
        long bookingId = 2L;
        boolean approved = true;

        when(bookingService.update(bookingId, approved, userId)).thenThrow(new UnavailableItemException("message"));

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"message\"}"));

        verify(bookingService, times(1)).update(bookingId, approved, userId);
    }

    @Test
    void badRequestIncorrectAction() throws Exception {
        long userId = 5L;
        long bookingId = 2L;
        boolean approved = true;

        when(bookingService.update(bookingId, approved, userId)).thenThrow(new IncorrectActionException("message"));

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"message\"}"));

        verify(bookingService, times(1)).update(bookingId, approved, userId);
    }

    @Test
    void badRequestValidation() throws Exception {
        long userId = 5L;
        long bookingId = 2L;
        boolean approved = true;

        when(bookingService.update(bookingId, approved, userId)).thenThrow(new IllegalArgumentException("message"));

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"message\"}"));

        verify(bookingService, times(1)).update(bookingId, approved, userId);
    }
}