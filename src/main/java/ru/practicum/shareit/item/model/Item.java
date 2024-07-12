package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@RequiredArgsConstructor
public class Item {
    Long id;
    @NotNull
    String name;
    String description;
    @NotNull
    Boolean available;
    @NotNull
    User owner;
    ItemRequest request;
}
