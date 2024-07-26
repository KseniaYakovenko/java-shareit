package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemDtoMapper.dtoToItem;
import static ru.practicum.shareit.item.dto.ItemDtoMapper.toItemDto;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        Item item = dtoToItem(itemDto);
        User owner = userRepository.getById(userId);
        item.setOwner(owner);
        return toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId) {

        Long id = itemDto.getId();
        Item oldItem = itemRepository.getById(id);
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

        return toItemDto(itemRepository.update(item));
    }

    @Override
    public ItemDto getById(long itemId) {
        return toItemDto(itemRepository.getById(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.getUserItems(userId).stream().map(ItemDtoMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(query).stream().map(ItemDtoMapper::toItemDto).collect(Collectors.toList());
    }
}
