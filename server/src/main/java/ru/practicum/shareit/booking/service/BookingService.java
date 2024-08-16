package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getBookings(long userId, BookingState state);

    List<BookingDto> getBookingsForOwner(long userId, BookingState state);

    BookingDto create(BookingRequestDto bookingRequestDto, long userId);

    BookingDto update(Long bookingId, boolean approved, long userId);

    BookingDto getBookingByIdAndUserId(Long bookingId, Long userId);
}