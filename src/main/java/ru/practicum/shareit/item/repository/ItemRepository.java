package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> search(String query);

    List<Item> getUserItems(long userId);

    Item getById(long itemId);

    Item update(Item item);

    Item create(Item item);

}
