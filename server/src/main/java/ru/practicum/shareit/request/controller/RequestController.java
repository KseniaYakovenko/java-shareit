package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ErrorResponse;
import ru.practicum.shareit.exception.IncorrectActionException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Collection;

import static ru.practicum.shareit.ErrorResponse.getErrorResponse;
import static ru.practicum.shareit.utils.Constants.USER_ID_HEADER;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;


    @PostMapping
    public RequestInfoDto create(@RequestBody @Validated RequestDto requestDto,
                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.create(requestDto, userId);
    }

    @GetMapping("/{requestId}")
    public RequestInfoDto getById(@PathVariable long requestId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getByRequestId(requestId, userId);
    }

    @GetMapping
    public Collection<RequestInfoDto> getAllByUser(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.findAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<RequestInfoDto> getAll(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.findAll();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(final NotFoundException e) {
        return getErrorResponse(e, log);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final IncorrectActionException e) {
        return getErrorResponse(e, log);
    }
}
