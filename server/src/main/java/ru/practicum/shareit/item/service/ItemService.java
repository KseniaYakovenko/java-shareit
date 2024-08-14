package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId);

    ItemInfoDto getById(long itemId, long userId);

    List<ItemInfoDto> getUserItems(long userId);

    List<ItemDto> search(String query);

    CommentDto createComment(CommentRequestDto commentRequestDto, long itemId, long userId);
}