package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingDtoMapper.toBookingDto;
import static ru.practicum.shareit.item.dto.CommentDtoMapper.toCommentDto;
import static ru.practicum.shareit.item.dto.ItemDtoMapper.*;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long userId) {
        Item item = dtoToItem(itemDto);
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Нет пользователя с id = " + userId));
        item.setOwner(owner);
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            Request request = requestRepository.findById(requestId).orElseThrow(() ->
                    new NotFoundException("Нет request с id = " + requestId));
            item.setRequest(request);
        }
        return toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long userId) {

        Long id = itemDto.getId();
        Item oldItem = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Нет item с id = " + id));
        Item item = dtoToItem(itemDto);

        User owner = oldItem.getOwner();
        if (owner.getId() != userId) {
            throw new AccessDeniedException("Пользователь c id " + userId + " не имеет прав редактирования.");
        }

        if (itemDto.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (itemDto.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        if (itemDto.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        item.setOwner(owner);

        return toItemDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemInfoDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Нет item с id = " + itemId));

        ItemInfoDto itemInfoDto = toItemInfoDto(item);

        List<Booking> bookings = bookingRepository.findAllByItemId(item.getId()).stream()
                .filter(booking -> !booking.getStatus().equals(BookingStatus.REJECTED.name()))
                .collect(Collectors.toList());
        if (item.getOwner().getId().equals(userId)) {
            setLastNextBooking(bookings, itemInfoDto);
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        List<CommentDto> commentDtoList = comments.stream().map(CommentDtoMapper::toCommentDto).toList();
        itemInfoDto.setComments(commentDtoList);
        return itemInfoDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemInfoDto> getUserItems(long userId) {
        List<ItemInfoDto> itemInfoDtos = itemRepository.findByOwnerId(userId).stream().map(ItemDtoMapper::toItemInfoDto).toList();
        List<Long> itemIds = itemInfoDtos.stream().map(ItemInfoDto::getId).toList();
        List<Booking> bookings = bookingRepository.findAllByItemIds(itemIds);
        itemInfoDtos.forEach(
                item -> setLastNextBooking(bookings, item)
        );
        return itemInfoDtos.stream().sorted(Comparator.comparing(ItemInfoDto::getId)).collect(Collectors.toList());
    }

    private static void setLastNextBooking(List<Booking> bookings, ItemInfoDto item) {
        List<Booking> bks = bookings.stream().filter(b ->
                Objects.equals(b.getItem().getId(), item.getId())).toList();
        bks.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart))
                .ifPresent(last -> item.setLastBooking(toBookingDto(last)));

        bks.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getFinish))
                .ifPresent(next -> item.setNextBooking(toBookingDto(next)));
    }

    @Override
    public List<ItemDto> search(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemDtoMapper::toItemDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto createComment(CommentRequestDto commentRequestDto, long itemId, long userId) {
        boolean commentAllowed = commentRepository.existFinishedBooking(itemId, userId);
        if (!commentAllowed) {
            throw new IncorrectActionException("Невозможно оставить отзыв");
        }
        User author = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(author);
        return toCommentDto(commentRepository.save(comment));
    }
}
