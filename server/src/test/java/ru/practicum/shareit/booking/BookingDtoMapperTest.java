package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

import static ru.practicum.shareit.booking.dto.BookingDtoMapper.*;

@RequiredArgsConstructor
@SpringBootTest(classes = BookingDtoMapper.class)
public class BookingDtoMapperTest {

    @Test
    void toDto() {
        Booking booking = new Booking();
        BookingStatus status = BookingStatus.REJECTED;
        booking.setStatus(status.name());
        long bookId = 1000L;
        long itemId = 2000L;
        Item item = new Item();
        String name = "UserName";
        String description = "user@email.ru";
        Boolean available = true;
        item.setId(itemId);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        booking.setId(bookId);
        booking.setItem(item);
        BookingDto bookingDto = toBookingDto(booking);
        Assertions.assertEquals(bookId, bookingDto.getId());
        Assertions.assertEquals(status, bookingDto.getStatus());
    }

    @Test
    void toBooking() {
        BookingRequestDto bookingDto  = new BookingRequestDto();
        BookingStatus status = BookingStatus.APPROVED;
        long bookerId = 3000L;
        LocalDateTime start = LocalDateTime.now();
        bookingDto.setBookerId(bookerId);
         bookingDto.setStart(start);
        Booking booking = dtoToBooking(bookingDto);
        Assertions.assertEquals(bookerId, booking.getBookerId());
        Assertions.assertEquals(start, booking.getStart());
    }

}
