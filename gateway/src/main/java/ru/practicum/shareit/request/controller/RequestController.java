package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated RequestDto requestDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.create(requestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable long requestId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getByRequestId(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.findAllRequestsByUserId(userId);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getAll();
    }
}
