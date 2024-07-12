package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private Long generatorId = 0L;

    public Long generateId() {
        return ++generatorId;
    }

    @Override
    public List<Item> search(String query) {
        String queryLC = query.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(queryLC) ||
                                item.getDescription().toLowerCase().contains(queryLC))
                ).toList();
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == userId).toList();
    }

    @Override
    public Item getById(long itemId) {
        Item item = items.get(itemId);
        if (item == null) throw new NotFoundException("Нет item с id = " + itemId);
        return item;
    }

    @Override
    public Item update(Item item) {
        long itemId = item.getId();
        items.put(itemId, item);
        return items.get(itemId);
    }

    @Override
    public Item create(Item item) {
        Long itemId = generateId();
        item.setId(itemId);
        items.put(itemId, item);
        return items.get(itemId);
    }
}
