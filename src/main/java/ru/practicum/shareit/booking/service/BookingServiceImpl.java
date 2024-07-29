package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnAvailableItemException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingDtoMapper.dtoToBooking;
import static ru.practicum.shareit.booking.dto.BookingDtoMapper.toBookingDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> getBookings(long bookerId, BookingState state) {
        List<BookingDto> bookingDtos = bookingRepository.findByBookerId(bookerId).stream().map(
                BookingDtoMapper::toBookingDto).collect(Collectors.toList());
        return filteredByState(state, bookingDtos).stream().sorted(Comparator.comparing(BookingDto::getStart).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsForOwner(long ownerId, BookingState state) {
        List<BookingDto> bookingDtos = bookingRepository.findByOwnerId(ownerId).stream().map(
                BookingDtoMapper::toBookingDto).collect(Collectors.toList());
        if (bookingDtos.isEmpty()) {
            throw new NotFoundException("Нет бронирований");
        }
        return filteredByState(state, bookingDtos).stream().sorted(Comparator.comparing(BookingDto::getStart).reversed()).collect(Collectors.toList());
    }

    private static List<BookingDto> filteredByState(BookingState state, List<BookingDto> bookingDtos) {
        switch (state) {
            case PAST:
                return bookingDtos.stream().filter(bookingDto -> bookingDto.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingDtos.stream().filter(bookingDto -> bookingDto.getStart().isBefore(LocalDateTime.now())
                                && bookingDto.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingDtos.stream().filter(bookingDto -> bookingDto.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingDtos.stream().filter(bookingDto -> bookingDto.getStatus().equals(BookingStatus.REJECTED))
                        .collect(Collectors.toList());
            case WAITING:
                return bookingDtos.stream().filter(bookingDto -> bookingDto.getStatus().equals(BookingStatus.WAITING))
                        .collect(Collectors.toList());
            default:
                return bookingDtos;
        }
    }

    @Override
    @Transactional
    public BookingDto create(BookingRequestDto bookingRequestDto, long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Нет пользователя с id = " + userId));
        bookingRequestDto.setBookerId(userId);
        Booking booking = dtoToBooking(bookingRequestDto);
        booking.setStart(bookingRequestDto.getStart());
        booking.setFinish(bookingRequestDto.getEnd());
        booking.setStatus(BookingStatus.WAITING.name());
        Long itemId = bookingRequestDto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Нет item с id = " + itemId));
        if (!item.getAvailable()) {
            throw new UnAvailableItemException("item с id = " + itemId + " недоступен для бронирования");
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("item с id = " + itemId + " недоступен для бронирования владельцем");
        }
        booking.setItem(item);
        return toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto update(Long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findByOwnerIdAndBookingId(userId, bookingId);
        if (booking == null) {
            throw new NotFoundException("no booking");
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED.name())) {
            throw new IncorrectActionException("Status already set");
        }
        BookingStatus status;

        if (approved) {
            status = BookingStatus.APPROVED;
        } else {
            status = BookingStatus.REJECTED;
        }
        booking.setStatus(status.name());
        bookingRepository.save(booking);
        return toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingByIdAndUserId(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId);
        if (booking == null) {
            throw new NotFoundException("no booking");
        }
        return toBookingDto(booking);
    }
}