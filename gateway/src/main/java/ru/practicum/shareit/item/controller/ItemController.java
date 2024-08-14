package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validator.Marker;


@Slf4j
@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(Marker.OnCreate.class) ItemDto itemDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable long itemId,
                                         @RequestBody ItemDto itemDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {

        itemDto.setId(itemId);
        return itemClient.update(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String query) {
        return itemClient.search(query);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable long itemId,
                                                @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createComment(commentRequestDto, itemId, userId);
    }
}
