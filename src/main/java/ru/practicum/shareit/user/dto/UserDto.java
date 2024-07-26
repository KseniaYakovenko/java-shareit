package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validator.Marker;

@Data
@AllArgsConstructor
public class UserDto {
    Long id;
    String name;
    @NotNull(groups = Marker.OnCreate.class)
    @Email(groups = {Marker.OnUpdate.class, Marker.OnCreate.class})
    String email;
}
