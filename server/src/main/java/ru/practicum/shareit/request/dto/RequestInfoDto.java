package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@Data
public class RequestInfoDto {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private Collection<ItemInfoDto> items;
}
