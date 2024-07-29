package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;


public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto bookingRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingRequestDto.getStart();
        LocalDateTime end = bookingRequestDto.getEnd();
        if ((start == null) || (end == null)) {
            return false;
        }
        return bookingRequestDto.getStart().isBefore(bookingRequestDto.getEnd());
    }
}