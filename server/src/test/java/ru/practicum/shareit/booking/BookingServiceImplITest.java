package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@SpringBootTest
public class BookingServiceImplITest {
    @Autowired
    private EntityManager em;
    @Autowired
    private BookingService service;
    private User user;
    private User owner;
    private Item item;


    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setName("UserName");
        user.setEmail("user@email.ru");
        em.persist(user);

        owner = new User();
        owner.setName("ownerName");
        owner.setEmail("owner@email.ru");
        em.persist(owner);

        item = new Item();
        item.setName("itemName");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        em.persist(item);
    }

    @Test
    void createBooking() {
        Long itemId = item.getId();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long bookerId = user.getId();
        BookingRequestDto bookingRequestDto = new BookingRequestDto(itemId, start, end, bookerId);
        BookingDto bookingDto = service.create(bookingRequestDto, bookerId);
        Assertions.assertEquals(1L, bookingDto.getId());
        Assertions.assertEquals(start, bookingDto.getStart());
        Assertions.assertEquals(end, bookingDto.getEnd());
        Assertions.assertEquals(item.getId(), bookingDto.getItem().id());
        Assertions.assertEquals(item.getName(), bookingDto.getItem().name());
        Assertions.assertEquals(user.getId(), bookingDto.getBookerId());
        Assertions.assertEquals(BookingStatus.WAITING, bookingDto.getStatus());
    }

    @Test
    void updateBooking() {
        Long itemId = item.getId();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long bookerId = user.getId();
        BookingRequestDto bookingRequestDto = new BookingRequestDto(itemId, start, end, bookerId);
        BookingDto bookingDto = service.create(bookingRequestDto, bookerId);
        Long bookingId = bookingDto.getId();
        BookingDto updatedBookingDto = service.update(bookingId, true, owner.getId());
        Assertions.assertEquals(start, updatedBookingDto.getStart());
        Assertions.assertEquals(end, updatedBookingDto.getEnd());
        Assertions.assertEquals(item.getId(), updatedBookingDto.getItem().id());
        Assertions.assertEquals(item.getName(), updatedBookingDto.getItem().name());
        Assertions.assertEquals(user.getId(), updatedBookingDto.getBookerId());
        Assertions.assertEquals(BookingStatus.APPROVED, updatedBookingDto.getStatus());
    }

    @Test
    void getBookings() {
        User testBookerUser = new User();
        testBookerUser.setName("UserName");
        testBookerUser.setEmail("testBookerUser@email.ru");
        em.persist(testBookerUser);
        Long itemId = item.getId();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long testBookerId = testBookerUser.getId();
        BookingRequestDto bookingRequestDto = new BookingRequestDto(itemId, start, end, testBookerId);
        BookingDto bookingDto1 = service.create(bookingRequestDto, testBookerId);
        BookingDto bookingDto2 = service.create(bookingRequestDto, testBookerId);
        List<Long> bookingIdsExpected = List.of(bookingDto1.getId(), bookingDto2.getId());
        List<BookingDto> bookings = service.getBookings(testBookerId, BookingState.ALL);
        Assertions.assertEquals(2, bookings.size());
        List<Long> bookingIdsActual = bookings.stream().map(BookingDto::getId).toList();
        Assertions.assertEquals(bookingIdsExpected, bookingIdsActual);
    }

    @Test
    void getBookingsForOwner() {
        Long itemId = item.getId();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long bookerId = user.getId();
        BookingRequestDto bookingRequestDto = new BookingRequestDto(itemId, start, end, bookerId);
        service.create(bookingRequestDto, bookerId);
        List<BookingDto> bookings = service.getBookingsForOwner(owner.getId(), BookingState.ALL);
        Assertions.assertEquals(1, bookings.size());
        Long actualId = bookings.get(0).getItem().id();
        Assertions.assertEquals(actualId, itemId);
    }

    @Test
    void getBookingByIdAndUserId() {
        Long itemId = item.getId();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Long bookerId = user.getId();
        BookingRequestDto bookingRequestDto = new BookingRequestDto(itemId, start, end, bookerId);
        BookingDto bookingDto = service.create(bookingRequestDto, bookerId);
        BookingDto booking = service.getBookingByIdAndUserId(bookingDto.getId(), user.getId());
        System.out.println(booking);
        Assertions.assertEquals(bookingDto.getId(), booking.getId());
        Assertions.assertEquals(bookerId, booking.getBookerId());
    }
}
