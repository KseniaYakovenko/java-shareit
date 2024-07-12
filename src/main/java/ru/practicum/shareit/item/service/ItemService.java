package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId);

    ItemDto getById(long itemId);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> search(String query);
}