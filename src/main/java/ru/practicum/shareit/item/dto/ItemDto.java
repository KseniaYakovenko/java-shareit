package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validator.Marker;

@AllArgsConstructor
@Data
public class ItemDto {
    Long id;
    @Pattern(regexp = "\\S+", groups = Marker.OnCreate.class)
    String name;
    @NotNull(groups = Marker.OnCreate.class)
    String description;
    @NotNull(groups = Marker.OnCreate.class)
    Boolean available;
    Long requestId;
}
