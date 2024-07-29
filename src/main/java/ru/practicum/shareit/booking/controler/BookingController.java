package ru.practicum.shareit.booking.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncorrectActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody @Valid BookingRequestDto bookingRequestDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.create(bookingRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approved(@PathVariable Long bookingId, @RequestParam boolean approved,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.update(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBookingByIdAndUserId(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam);
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam);
        return bookingService.getBookingsForOwner(userId, state);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final UnavailableItemException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final IncorrectActionException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorResponse.setStacktrace(pw.toString());
        return errorResponse;
    }
}