package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.StartBeforeEnd;

import java.time.LocalDateTime;

@StartBeforeEnd
@AllArgsConstructor
@Setter
@Getter
public class BookingRequestDto {
    private Long itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Long bookerId;
}