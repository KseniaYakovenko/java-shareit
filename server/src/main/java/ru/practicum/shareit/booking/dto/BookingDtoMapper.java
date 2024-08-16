package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;

public class BookingDtoMapper {
    private BookingDtoMapper() {
    }

    public static BookingDto toBookingDto(Booking booking) {

        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getFinish());
        bookingDto.setBooker(new BookingDto.Booker(booking.getBookerId()));
        bookingDto.setBookerId(booking.getBookerId());
        bookingDto.setStatus(BookingStatus.from(booking.getStatus()));
        Item item = booking.getItem();
        bookingDto.setItem(new BookingDto.Item(item.getId(), item.getName()));
        return bookingDto;

    }

    public static Booking dtoToBooking(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setFinish(bookingRequestDto.getEnd());
        booking.setBookerId(bookingRequestDto.getBookerId());
        return booking;
    }
}