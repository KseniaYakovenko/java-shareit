package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exception.IncorrectActionException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.validator.Marker;

import java.util.List;

import static ru.practicum.shareit.ErrorResponse.getErrorResponse;
import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;


@Slf4j
@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    public ItemDto create(@RequestBody @Validated(Marker.OnCreate.class) ItemDto itemDto,
                          @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader(USER_ID_HEADER) long userId) {

        itemDto.setId(itemId);
        return itemService.update(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getById(@PathVariable long itemId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getAllUserItems(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String query) {
        return itemService.search(query);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable long itemId,
                                    @RequestBody CommentRequestDto commentRequestDto,
                                    @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.createComment(commentRequestDto, itemId, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e) {
        return getErrorResponse(e, log);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleException(final AccessDeniedException e) {
        return getErrorResponse(e, log);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final IncorrectActionException e) {
        return getErrorResponse(e, log);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final ConstraintViolationException e) {
        return getErrorResponse(e, log);
    }
}
